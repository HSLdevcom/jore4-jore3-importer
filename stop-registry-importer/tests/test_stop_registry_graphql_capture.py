#!/usr/bin/env python3

import json
import os
import socket
import subprocess
import sys
import tempfile
import time
import unittest
from pathlib import Path

import pymssql


ROOT_DIR = Path(__file__).resolve().parents[2]
IMPORTER_DIR = ROOT_DIR / "stop-registry-importer"
IMPORTER_FILE = IMPORTER_DIR / "importer.py"
MOCK_SERVER_FILE = IMPORTER_DIR / "tests" / "mock_graphql_server.py"
JORE3_SQL_DIR = ROOT_DIR / "src" / "test" / "resources" / "sql" / "jore3"
JORE4_SQL_DIR = ROOT_DIR / "src" / "test" / "resources" / "sql" / "jore4"
MSSQL_CONTAINER_NAME = "mssqltestdb"
TESTDB_CONTAINER_NAME = "testdb"
DOCKER_COMPOSE_FILES = [
    ROOT_DIR / "docker" / "docker-compose.yml",
    ROOT_DIR / "docker" / "docker-compose.custom.yml",
]

JORE3_FIXTURE_SQL_FILES = [
    "drop_tables.sql",
    "populate_nodes.sql",
    "populate_stop_places.sql",
    "populate_scheduled_stop_points.sql",
    "populate_equipment_details.sql",
    "populate_accessibility_information.sql"
]

JORE4_FIXTURE_SQL_FILES = [
    "drop_tables.sql",
    "populate_timing_places.sql",
    "populate_infrastructure_links.sql",
    "populate_imported_stop_points.sql"
]

def compose_command(*args):
    cmd = ["docker", "compose"]
    for compose_file in DOCKER_COMPOSE_FILES:
        cmd.extend(["-f", str(compose_file)])
    cmd.extend(args)
    return cmd


def wait_for_port(host, port, timeout_seconds):
    end_time = time.time() + timeout_seconds
    while time.time() < end_time:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.settimeout(1)
            if sock.connect_ex((host, port)) == 0:
                return
        time.sleep(1)
    raise TimeoutError(f"Timed out waiting for {host}:{port}")


def wait_for_mssql(host, port, username, password, database, timeout_seconds):
    end_time = time.time() + timeout_seconds
    while time.time() < end_time:
        try:
            with pymssql.connect(f"{host}:{port}", username, password, database, timeout=3):
                return
        except Exception:
            time.sleep(2)
    raise TimeoutError("Timed out waiting for MSSQL to accept connections")


def build_stop_points_fixture(host, port, username, password, database):
    query = """
    SELECT TOP 500
        s.solkirjain,
        s.sollistunnus,
        s.solomx,
        s.solomy
    FROM jr_pysakki p
    INNER JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
    WHERE s.solkirjain IS NOT NULL
      AND s.sollistunnus IS NOT NULL
      AND s.solomx IS NOT NULL
      AND s.solomy IS NOT NULL
    ORDER BY p.pysviimpvm ASC
    """

    stop_points = []
    seen_labels = set()

    with pymssql.connect(f"{host}:{port}", username, password, database) as conn:
        with conn.cursor(as_dict=True) as cursor:
            cursor.execute(query)
            for row in cursor.fetchall():
                label = f"{row['solkirjain']}{row['sollistunnus']}"
                if label in seen_labels:
                    continue
                seen_labels.add(label)
                stop_points.append(
                    {
                        "label": label,
                        "measured_location": {
                            "coordinates": [float(row["solomy"]), float(row["solomx"])],
                        },
                        "priority": 10,
                        "validity_start": "2020-01-01",
                        "validity_end": "2035-12-31",
                    }
                )

    if not stop_points:
        raise AssertionError("No stop points could be prepared from MSSQL fixture data")

    return stop_points


def run_sqlcmd_file(container_name, database, sql_file_in_container):
    command = [
        "docker",
        "exec",
        container_name,
        "/opt/mssql-tools18/bin/sqlcmd",
        "-S",
        "localhost",
        "-U",
        "sa",
        "-P",
        "P@ssw0rd",
        "-C",
        "-b",
        "-d",
        database,
        "-i",
        sql_file_in_container,
    ]
    subprocess.run(
        command,
        cwd=ROOT_DIR,
        check=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
    )


def run_sqlcmd_query(container_name, database, query):
    command = [
        "docker",
        "exec",
        container_name,
        "/opt/mssql-tools18/bin/sqlcmd",
        "-S",
        "localhost",
        "-U",
        "sa",
        "-P",
        "P@ssw0rd",
        "-C",
        "-b",
        "-d",
        database,
        "-Q",
        query,
    ]
    subprocess.run(
        command,
        cwd=ROOT_DIR,
        check=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
    )


def run_psql_file(container_name, database, sql_file_path):
    command = [
        "docker",
        "exec",
        "-e",
        "PGPASSWORD=adminpassword",
        "-i",
        container_name,
        "psql",
        "-h",
        "localhost",
        "-U",
        "dbadmin",
        "-d",
        database,
        "-v",
        "ON_ERROR_STOP=1",
    ]
    with sql_file_path.open("rb") as sql_file:
        subprocess.run(
            command,
            cwd=ROOT_DIR,
            check=True,
            stdin=sql_file,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
        )


def populate_jore3_from_sql_fixtures(container_name, database):
    subprocess.run(
        ["docker", "exec", container_name, "mkdir", "-p", "/tmp/jore3-fixtures"],
        cwd=ROOT_DIR,
        check=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
    )

    for sql_file_name in JORE3_FIXTURE_SQL_FILES:
        host_file = JORE3_SQL_DIR / sql_file_name
        target_file = f"/tmp/jore3-fixtures/{sql_file_name}"
        subprocess.run(
            ["docker", "cp", str(host_file), f"{container_name}:{target_file}"],
            cwd=ROOT_DIR,
            check=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
        )
        run_sqlcmd_file(container_name, database, target_file)


def populate_jore4_from_sql_fixtures(container_name, database):
    for sql_file_name in JORE4_FIXTURE_SQL_FILES:
        run_psql_file(container_name, database, JORE4_SQL_DIR / sql_file_name)


def docker_container_exists(container_name):
    result = subprocess.run(
        ["docker", "inspect", container_name],
        cwd=ROOT_DIR,
        check=False,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True,
    )
    return result.returncode == 0


def readKeyValue(keyValues, key):
    try:
        return next(x for x in keyValues if x.get("key") == key).get("values")[0]
    except:
        return None


class StopRegistryImporterGraphqlCaptureTest(unittest.TestCase):
    def test_importer_graphql_capture_with_real_databases(self):
        captured_requests = []
        started_mssql_via_compose = False
        started_testdb_via_compose = False
        mssql_host = "127.0.0.1"
        mssql_port = 1433
        mssql_username = "sa"
        mssql_password = "P@ssw0rd"
        mssql_database = "jore3testdb"

        try:
            if docker_container_exists(MSSQL_CONTAINER_NAME):
                subprocess.run(
                    ["docker", "start", MSSQL_CONTAINER_NAME],
                    cwd=ROOT_DIR,
                    check=False,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                )
            else:
                subprocess.run(
                    compose_command("up", "-d", "jore4-mssqltestdb"),
                    cwd=ROOT_DIR,
                    check=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                )
                started_mssql_via_compose = True

            if docker_container_exists(TESTDB_CONTAINER_NAME):
                subprocess.run(
                    ["docker", "start", TESTDB_CONTAINER_NAME],
                    cwd=ROOT_DIR,
                    check=False,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                )
            else:
                subprocess.run(
                    compose_command("up", "-d", "jore4-testdb"),
                    cwd=ROOT_DIR,
                    check=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                )
                started_testdb_via_compose = True

            wait_for_port(mssql_host, mssql_port, timeout_seconds=180)
            wait_for_mssql(
                mssql_host,
                mssql_port,
                mssql_username,
                mssql_password,
                mssql_database,
                timeout_seconds=180,
            )

            populate_jore4_from_sql_fixtures(TESTDB_CONTAINER_NAME, "jore4main")

            populate_jore3_from_sql_fixtures(MSSQL_CONTAINER_NAME, mssql_database)

            stop_points = build_stop_points_fixture(
                mssql_host,
                mssql_port,
                mssql_username,
                mssql_password,
                mssql_database,
            )

            with tempfile.TemporaryDirectory(prefix="stop-registry-capture-") as temp_dir:
                fixture_file = Path(temp_dir) / "fixture.json"
                capture_file = Path(temp_dir) / "captured-graphql.jsonl"
                fixture_file.write_text(
                    json.dumps(
                        {
                            "stop_points": stop_points,
                            "organisations": {},
                        }
                    ),
                    encoding="utf-8",
                )

                mock_server = subprocess.Popen(
                    [
                        sys.executable,
                        str(MOCK_SERVER_FILE),
                        "--host",
                        "127.0.0.1",
                        "--port",
                        "3900",
                        "--fixture",
                        str(fixture_file),
                        "--capture",
                        str(capture_file),
                    ],
                    cwd=ROOT_DIR,
                    stdout=subprocess.DEVNULL,
                    stderr=subprocess.DEVNULL,
                )

                try:
                    wait_for_port("127.0.0.1", 3900, timeout_seconds=30)

                    env = os.environ.copy()
                    env.update(
                        {
                            "STOP_REGISTRY_IMPORTER_USE_DOTENV": "0",
                            "HASURA_API_URL": "http://127.0.0.1:3900/v1/graphql",
                            "HASURA_ADMIN_SECRET": "hasura",
                            "SOURCE_DB_USERNAME": mssql_username,
                            "SOURCE_DB_PASSWORD": mssql_password,
                            "SOURCE_DB_HOSTNAME": mssql_host,
                            "SOURCE_DB_PORT": str(mssql_port),
                            "SOURCE_DB_DATABASE": mssql_database,
                            "STOP_REGISTRY_IMPORTER_DEBUG_RESULT_COUNTS": "1",
                        }
                    )

                    result = subprocess.run(
                        [sys.executable, str(IMPORTER_FILE)],
                        cwd=IMPORTER_DIR,
                        env=env,
                        check=False,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        text=True,
                    )

                    print(result.stdout)

                    self.assertEqual(
                        0,
                        result.returncode,
                        msg=f"Importer execution failed:\n{result.stdout}",
                    )

                finally:
                    mock_server.terminate()
                    mock_server.wait(timeout=10)

                captured_requests = [
                    json.loads(line)
                    for line in capture_file.read_text(encoding="utf-8").splitlines()
                    if line.strip()
                ]
        finally:
            if started_mssql_via_compose and started_testdb_via_compose:
                subprocess.run(
                    compose_command("stop", "jore4-mssqltestdb", "jore4-testdb"),
                    cwd=ROOT_DIR,
                    check=False,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                )
            else:
                if started_mssql_via_compose:
                    subprocess.run(
                        compose_command("stop", "jore4-mssqltestdb"),
                        cwd=ROOT_DIR,
                        check=False,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        text=True,
                    )
                if started_testdb_via_compose:
                    subprocess.run(
                        compose_command("stop", "jore4-testdb"),
                        cwd=ROOT_DIR,
                        check=False,
                        stdout=subprocess.PIPE,
                        stderr=subprocess.STDOUT,
                        text=True,
                    )

        self.assertGreater(len(captured_requests), 0, "No GraphQL requests were captured")

        stop_point_queries = [
            req for req in captured_requests if "service_pattern_scheduled_stop_point" in req.get("query", "")
        ]
        self.assertGreater(len(stop_point_queries), 0, "Importer did not request scheduled stop points")

        stop_place_mutations = [
            req for req in captured_requests if "mutation AddStopPlace" in req.get("query", "")
        ]
        self.assertEqual(len(stop_place_mutations), 1, "Importer did not send expected number of stop place mutations")
        
        for mutation in stop_place_mutations:
            variables = mutation.get("variables", {})
            self.assertEqual(variables.get("stopName"), "Yliopisto", "Stop place mutation name mismatch")
            self.assertEqual(variables.get("stopNameSwe"), "Universitetet", "Stop place mutation stopNameSwe mismatch")
            self.assertEqual(variables.get("longName"), "Yliopiston pitkä nimi", "Stop place mutation longName mismatch")
            self.assertEqual(variables.get("longNameSwe"), "Universitetets långt namn", "Stop place mutation longNameSwe mismatch")
            self.assertEqual(variables.get("validityStart"), "2020-01-01", "Stop place mutation validityStart mismatch")
            self.assertEqual(variables.get("validityEnd"), "2035-12-31", "Stop place mutation validityEnd mismatch")
            self.assertEqual(variables.get("coordinates"), [6.0, 5.0], "Stop place mutation measuredLocation.coordinates mismatch")
            quays = variables.get("quays", [])
            self.assertEqual(len(quays), 1, "Stop place mutation did not have 1 quay as expected")
            for quay in quays:
                self.assertEqual(quay.get("publicCode"), "H1234", "Quay mutation publicCode mismatch")
                self.assertEqual(quay.get("privateCode", {}).get("value").strip(), "c", "Quay mutation privateCode.value mismatch")
                self.assertEqual(quay.get("geometry", {}).get("coordinates"), [6.0, 5.0], "Quay mutation measuredLocation.coordinates mismatch")

                keyValues = quay.get("keyValues", [])
                self.assertEqual(readKeyValue(keyValues, "priority"), "10", "Quay mutation keyValues priority mismatch")
                self.assertEqual(readKeyValue(keyValues, "validityStart"), "2020-01-01", "Quay mutation keyValues validityStart mismatch")
                self.assertEqual(readKeyValue(keyValues, "validityEnd"), "2035-12-31", "Quay mutation keyValues validityEnd mismatch")
                self.assertEqual(readKeyValue(keyValues, "elyNumber"), "1234567890", "Quay mutation keyValues elyNumber mismatch")
                self.assertEqual(readKeyValue(keyValues, "stopState"), "InOperation", "Quay mutation keyValues stopState mismatch")
                self.assertEqual(readKeyValue(keyValues, "mainLine"), "true", "Quay mutation keyValues mainLine mismatch")
                self.assertEqual(readKeyValue(keyValues, "postalCode"), None, "Quay mutation keyValues postalCode mismatch")
                self.assertEqual(readKeyValue(keyValues, "functionalArea"), "1", "Quay mutation keyValues functionalArea mismatch")
                self.assertEqual(readKeyValue(keyValues, "streetAddress"), "Unioninkatu", "Quay mutation keyValues streetAddress mismatch")
                self.assertEqual(readKeyValue(keyValues, "stopOwner"), "finavia", "Quay mutation keyValues stopOwner mismatch")

                placeEquipments = quay.get("placeEquipments", {})
                shelterEquipment = placeEquipments.get("shelterEquipment", [])
                self.assertEqual(len(shelterEquipment), 2, "Quay mutation placeEquipments did not have 2 shelterEquipment as expected")
                self.assertEqual(shelterEquipment[0].get("shelterExternalId"), "1234, 5678", "Quay mutation placeEquipments first shelter shelterExternalId mismatch")
                self.assertEqual(shelterEquipment[1].get("shelterExternalId"), None, "Quay mutation placeEquipments second shelter shelterExternalId mismatch")

                for index, shelter in enumerate(shelterEquipment):
                    self.assertEqual(shelter.get("enclosed"), True, f"Quay mutation shelter {index} placeEquipments enclosed mismatch")
                    self.assertEqual(shelter.get("shelterType"), "steel", f"Quay mutation shelter {index} placeEquipments shelterType mismatch")
                    self.assertEqual(shelter.get("shelterElectricity"), "continuous", f"Quay mutation shelter {index} placeEquipments shelterElectricity mismatch")
                    self.assertEqual(shelter.get("shelterLighting"), True, f"Quay mutation shelter {index} placeEquipments shelterLighting mismatch")
                    self.assertEqual(shelter.get("shelterCondition"), "bad", f"Quay mutation shelter {index} placeEquipments shelterCondition mismatch")
                    self.assertEqual(shelter.get("timetableCabinets"), 1, f"Quay mutation shelter {index} placeEquipments timetableCabinets mismatch")
                    self.assertEqual(shelter.get("trashCan"), True, f"Quay mutation shelter {index} placeEquipments trashCan mismatch")
                    self.assertEqual(shelter.get("shelterHasDisplay"), True, f"Quay mutation shelter {index} placeEquipments shelterHasDisplay mismatch")
                    self.assertEqual(shelter.get("bicycleParking"), False, f"Quay mutation shelter {index} placeEquipments bicycleParking mismatch")
                    self.assertEqual(shelter.get("leaningRail"), True, f"Quay mutation shelter {index} placeEquipments leaningRail mismatch")
                    self.assertEqual(shelter.get("outsideBench"), True, f"Quay mutation shelter {index} placeEquipments outsideBench mismatch")
                    self.assertEqual(shelter.get("shelterFasciaBoardTaping"), True, f"Quay mutation shelter {index} placeEquipments shelterFasciaBoardTaping mismatch")
                    self.assertEqual(shelter.get("stepFree"), True, f"Quay mutation shelter {index} placeEquipments stepFree mismatch")


                generalSign = placeEquipments.get("generalSign", {})
                self.assertEqual(generalSign.get("numberOfFrames"), 2, "Quay mutation placeEquipments generalSign numberOfFrames mismatch")
                self.assertEqual(generalSign.get("signContentType"), None, "Quay mutation placeEquipments generalSign signContentType mismatch")
                self.assertEqual(generalSign.get("note"), "Additional maintenance info", "Quay mutation placeEquipments generalSign note mismatch")
                self.assertEqual(generalSign.get("content"), None, "Quay mutation placeEquipments generalSign contnet mismatch")

                organisations = quay.get("organisations", [])
                self.assertEqual(len(organisations), 3, "Quay mutation did not have 3 organisations as expected")
                upkeepOrg = next((org for org in organisations if org.get("relationshipType") == "shelterMaintenance"), None)
                self.assertEqual(upkeepOrg.get("organisationId"), "org-vantaa", "Quay mutation shelterMaintenance organisationId mismatch")
                infoOrg = next((org for org in organisations if org.get("relationshipType") == "infoUpkeep"), None)
                self.assertEqual(infoOrg.get("organisationId"), "org-ely", "Quay mutation infoUpkeep organisationId mismatch")
                ownerOrg = next((org for org in organisations if org.get("relationshipType") == "owner"), None)
                self.assertEqual(ownerOrg.get("organisationId"), "org-finavia", "Quay mutation owner organisationId mismatch")

                accessibilityAssessment = quay.get("accessibilityAssessment", {})

                accessibilityLimitations = accessibilityAssessment.get("limitations", {})
                self.assertEqual(accessibilityLimitations.get("stepFreeAccess"), "false", "Quay mutation accessibilityAssessment accessibilityLimitations stepFreeAccess mismatch")
                self.assertEqual(accessibilityLimitations.get("wheelchairAccess"), "false", "Quay mutation accessibilityAssessment accessibilityLimitations wheelchairAccess mismatch")

                hslAccessibilityProperties = accessibilityAssessment.get("hslAccessibilityProperties", {})
                self.assertEqual(hslAccessibilityProperties.get("accessibilityLevel"), "mostlyAccessible", "Quay mutation accessibilityAssessment hslAccessibilityProperties accessibilityLevel mismatch")
                self.assertEqual(hslAccessibilityProperties.get("curbBackOfRailDistance"), 6.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties curbBackOfRailDistance mismatch")
                self.assertEqual(hslAccessibilityProperties.get("guidanceStripe"), True, "Quay mutation accessibilityAssessment hslAccessibilityProperties guidanceStripe mismatch")
                self.assertEqual(hslAccessibilityProperties.get("guidanceTiles"), True, "Quay mutation accessibilityAssessment hslAccessibilityProperties guidanceTiles mismatch")
                self.assertEqual(hslAccessibilityProperties.get("guidanceType"), "other", "Quay mutation accessibilityAssessment hslAccessibilityProperties guidanceType mismatch")
                self.assertEqual(hslAccessibilityProperties.get("lowerCleatHeight"), 15.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties lowerCleatHeight mismatch")
                self.assertEqual(hslAccessibilityProperties.get("mapType"), None, "Quay mutation accessibilityAssessment hslAccessibilityProperties mapType mismatch")
                self.assertEqual(hslAccessibilityProperties.get("pedestrianCrossingRampType"), None, "Quay mutation accessibilityAssessment hslAccessibilityProperties pedestrianCrossingRampType mismatch")
                self.assertEqual(hslAccessibilityProperties.get("platformEdgeWarningArea"), True, "Quay mutation accessibilityAssessment hslAccessibilityProperties platformEdgeWarningArea mismatch")
                self.assertEqual(hslAccessibilityProperties.get("serviceAreaLength"), 60.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties serviceAreaLength mismatch")
                self.assertEqual(hslAccessibilityProperties.get("serviceAreaStripes"), None, "Quay mutation accessibilityAssessment hslAccessibilityProperties serviceAreaStripes mismatch")
                self.assertEqual(hslAccessibilityProperties.get("serviceAreaWidth"), 80.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties serviceAreaWidth mismatch")
                self.assertEqual(hslAccessibilityProperties.get("shelterType"), "narrow", "Quay mutation accessibilityAssessment hslAccessibilityProperties shelterType mismatch")
                self.assertEqual(hslAccessibilityProperties.get("sidewalkAccessibleConnection"), True, "Quay mutation accessibilityAssessment hslAccessibilityProperties sidewalkAccessibleConnection mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopAreaLengthwiseSlope"), 3.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties stopAreaLengthwiseSlope mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopAreaSideSlope"), 2.5, "Quay mutation accessibilityAssessment hslAccessibilityProperties stopAreaSideSlope mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopAreaSurroundingsAccessible"), True, "Quay mutation accessibilityAssessment hslAccessibilityProperties stopAreaSurroundingsAccessible mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopElevationFromRailTop"), 6.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties stopElevationFromRailTop mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopElevationFromSidewalk"), 5.0, "Quay mutation accessibilityAssessment hslAccessibilityProperties stopElevationFromSidewalk mismatch")
                self.assertEqual(hslAccessibilityProperties.get("stopType"), "busBulb", "Quay mutation accessibilityAssessment hslAccessibilityProperties stopType mismatch")


        stop_point_updates = [
            req for req in captured_requests if "mutation UpdateStopRef" in req.get("query", "")
        ]
        if stop_place_mutations:
            self.assertGreater(len(stop_point_updates), 0, "Importer did not send stop point update mutations")


if __name__ == "__main__":
    unittest.main(verbosity=2)
