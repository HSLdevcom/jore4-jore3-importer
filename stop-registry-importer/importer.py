#!/usr/bin/env python3

"""
Stop Registry Importer — Jore3 → Jore4

Imports stop place data from a Jore3 MSSQL database into the Jore4 stop registry via a Hasura GraphQL API.

Configuration:
    Reads environment variables (or .env file) for the GraphQL endpoint, Hasura admin secret, and Jore3 DB credentials.

Data Retrieval:
    - get_jore3_stops()       — Queries the Jore3 MSSQL database, joining stop, node, stop area, equipment, and
                                accessibility tables. Returns stops grouped by stop area ID.
    - get_jore3_stop_areas()  — Queries Jore3 for stop areas that belong to network 1 and have more than one stop (i.e.
                                multi-quay areas). Yields rows as a generator.
    - get_jore4_stop_points() — Fetches existing scheduled stop points from Jore4 via GraphQL. Returns them as a dict
                                keyed by label.

Data Mapping:
    - quayInputForJore3Stop() — Transforms a single Jore3 stop row into the GraphQL QuayInput format, mapping fields
      like shelter type, electricity, condition, accessibility properties, and equipment using helper functions
      (mapStopModel, mapStopType, mapStopElectricity, mapStopCondition, mapBoolean, toFloat).

Import Loop (main logic):
    For each Jore3 stop area:
      1. Iterates over all stops in that area.
      2. Matches each stop to an existing Jore4 stop point by label (letter + number).
      3. Builds a list of quay inputs and collects coordinates / validity periods.
      4. update_stop_place() — Sends a GraphQL mutation to create/update the stop place with averaged coordinates,
         merged validity periods, and all quays attached.
      5. update_stop_point() — For each quay returned by the mutation, links the Jore4 scheduled stop point back to the
         new NeTEx ID via another GraphQL mutation.

Summary:
    Jore3 MSSQL → Python mapping → Jore4 Hasura GraphQL: migrates stop places (with quays, accessibility, equipment,
    and shelter data) from the legacy system into the new one, and cross-references the created stop places back to the
    existing scheduled stop points.

Jore3 Source Tables (MSSQL):
    The main query (get_jore3_stops) joins five tables:

        jr_pysakki (p)               — Stop properties (name, shelter, postal code, etc.)
        jr_solmu (s)                 — Node coordinates and label parts (solkirjain, sollistunnus)
        jr_lij_pysakkialue (pa)      — Stop area grouping (pysalueid)
        jr_varustelutiedot_uusi (vt) — Equipment details (shelter type, electricity, signs)
        jr_esteettomyys (e)          — Accessibility properties (slopes, curb distances) [LEFT JOIN]

    The stop area query (get_jore3_stop_areas) selects areas from network 1 with > 1 stop.

Jore4 Target (Hasura GraphQL — stop_registry.mutateStopPlace):

    Stop Place level mapping:
        Jore3 column         → GraphQL field                  Notes
        ─────────────────────────────────────────────────────────────────────
        pysalueid            → privateCode.value              Stop area ID (type: HSL/JORE-3)
        pysnimi              → name (lang: fin)               Stop name Finnish
        pysnimipitka         → alternativeNames[fin, alias]   Long name Finnish
        pysnimipitkar        → alternativeNames[swe, alias]   Long name Swedish
        pysnimir             → alternativeNames[swe, transl.] Stop name Swedish
        avg(quay coords)     → geometry.coordinates           Centroid of all quays
        min(validity_start)  → keyValues["validityStart"]     Earliest stop point start
        max(validity_end)    → keyValues["validityEnd"]       Latest stop point end
        verkko (via pa)      → transportMode                  mapTransportMode: 1→bus, 3→tram

    Quay level mapping (one quay per stop in the area):
        Jore3 column         → GraphQL field                  Notes
        ─────────────────────────────────────────────────────────────────────
        solkirjain +
          sollistunnus       → publicCode                     Stop label (e.g. "H1234")
        soltunnus            → privateCode.value              Node ID (type: HSL/JORE-3)
        pyspaikannimi        → description (lang: fin)        Place description Finnish
        pyspaikannimir       → alternativeNames[swe]          Place description Swedish
        postinro             → keyValues["postalCode"]        Postal code
        pyssade              → keyValues["functionalArea"]    Functional area radius
        pysosoite            → keyValues["streetAddress"]     Street address
        (from Jore4 SP)      → geometry.coordinates           Coordinates from stop point
        (from Jore4 SP)      → keyValues["validityStart"]     From existing scheduled stop point
        (from Jore4 SP)      → keyValues["validityEnd"]       From existing scheduled stop point
        (hardcoded)          → keyValues["stopState"]         Always "InOperation"
        (hardcoded)          → keyValues["mainLine"]          Always "false"
        (hardcoded)          → keyValues["virtual"]           Always "false"
        (hardcoded)          → keyValues["priority"]          Always "10"

    Accessibility assessment (per quay, from jr_esteettomyys):
        Jore3 column         → GraphQL field                   Conversion
        ─────────────────────────────────────────────────────────────────────
        korotus_ajorataan    → curbBackOfRailDistance          toFloat
        alapiena_korkeus     → lowerCleatHeight                toFloat
        varoitusalue         → platformEdgeWarningArea         mapBoolean (1→True, else→False)
        pituuskaltevuus      → stopAreaLengthwiseSlope         toFloat
        sivukaltevuus        → stopAreaSideSlope               toFloat
        esteeton_kulku       → stopAreaSurroundingsAccessible  mapBoolean
        korotus_kaytavaan    → stopElevationFromSidewalk       toFloat
        pysakin_malli        → stopType                        mapStopModel (see below)

    Limitations (all hardcoded to FALSE):
        audibleSignalsAvailable, escalatorFreeAccess, liftFreeAccess,
        stepFreeAccess, wheelchairAccess

    Shelter equipment (per quay, from jr_varustelutiedot_uusi):
        Jore3 column         → GraphQL field                   Conversion
        ─────────────────────────────────────────────────────────────────────
        pysakkityyppi        → shelterType                     mapStopType (see below)
        pysakkityyppi        → enclosed                        True if "01" or "02"
        sahko                → shelterElectricity              mapStopElectricity (see below)
        katos_kunto          → shelterCondition                mapStopCondition (see below)
        kpl_pysakkityyppi    → (shelter list multiplier)       Number of shelter copies
        lisavarusteet +
          kpl_lisavarusteet  → timetableCabinets               Count if lisavarusteet == "01", else 0
        roska_astia          → trashCan                        mapBoolean
        nayttolaitteet       → shelterHasDisplay               mapBoolean
        lisavarusteet        → bicycleParking                  True if "03" or "04"
        kpl_kilvet           → generalSign.numberOfFrames      toFloat
        (hardcoded)          → shelterLighting                 Always True
        (hardcoded)          → leaningRail                     Always False
        (hardcoded)          → outsideBench                    Always False
        (hardcoded)          → shelterFasciaBoardTaping        Always False

Value Mappings:

    mapTransportMode (verkko → transportMode):
        1 → "bus"    2 → "metro"    3 → "tram"    4 → "rail"    5 → "water"    else → "unknown"

    mapStopModel (pysakin_malli → stopType):
        "1" → "pullOut"    "2" → "busBulb"    "4" → "inLane"    else → "other"

    mapStopType (pysakkityyppi → shelterType):
        "01" → "glass"   "02" → "steel"   "04" → "post"      "05" → "urban"
        "06" → "concrete" "07" → "wooden"  else → "virtual"

    mapStopElectricity (sahko → shelterElectricity):
        "01" → "none"     "02" → "continuous"    else → "light"

    mapStopCondition (katos_kunto → shelterCondition):
        "01" → "good"     "02" → "mediocre"      else → "bad"

Post-import Linking:
    After creating each stop place, the returned quay NeTEx IDs are written back to the Jore4 scheduled stop points via:

        UPDATE service_pattern_scheduled_stop_point
        SET stop_place_ref = <quay NeTEx ID>
        WHERE label = <quay publicCode>
"""

import datetime
import pymssql
import requests
import simplejson as json
import environ

env = environ.Env(
    GRAPHQL_URL=(str,'http://localhost:3201/v1/graphql'),
    GRAPHQL_SECRET=(str,'hasura'),
    JORE3_USERNAME=(str,'sa'),
    JORE3_PASSWORD=(str,'P@ssw0rd'),
    JORE3_DATABASE_URL=(str,'localhost:1433'),
    JORE3_DATABASE_NAME=(str,'jore3testdb')
)

environ.Env.read_env('.env')

graphql = env('GRAPHQL_URL')
secret = env('GRAPHQL_SECRET')

jore3Username = env('JORE3_USERNAME')
jore3Password = env('JORE3_PASSWORD')
jore3DatabaseUrl = env('JORE3_DATABASE_URL')
jore3DatabaseName = env('JORE3_DATABASE_NAME')

def get_jore3_stops():

    stopPlaces = []
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:

        with conn.cursor(as_dict=True) as cursor:

            cursor.execute(f"""SELECT * FROM jr_pysakki p
            INNER JOIN jr_solmu s ON (p.soltunnus = s.soltunnus)
            INNER JOIN jr_lij_pysakkialue pa ON (p.pysalueid = pa.pysalueid)
            INNER JOIN jr_varustelutiedot_uusi vt ON (p.soltunnus = vt.tunnus)
            LEFT JOIN jr_esteettomyys e ON (p.soltunnus = e.tunnus)
            ORDER BY p.pysviimpvm ASC;""")

            stopPlaces = cursor.fetchall()

    print(f"Found {len(stopPlaces)} stop places")

    stopPlacesByArea = {}

    for x in stopPlaces:
        stopPlacesByArea.setdefault(x['pysalueid'], []).append(x)

    return stopPlacesByArea

def get_jore3_stop_areas():
    with pymssql.connect(jore3DatabaseUrl, jore3Username, jore3Password, jore3DatabaseName) as conn:

        with conn.cursor(as_dict=True) as cursor:

            cursor.execute("""SELECT pa.nimi, pa.pysalueid, pa.verkko, s.sollistunnus, s.solkirjain FROM jr_pysakki p
            INNER JOIN jr_solmu s ON (s.soltunnus = p.soltunnus)
            INNER JOIN jr_lij_pysakkialue pa ON (p.pysalueid = pa.pysalueid)
            WHERE p.pysalueid IN (
            SELECT jp.pysalueid FROM jr_pysakki jp
            INNER JOIN jr_lij_pysakkialue jlp ON (jp.pysalueid = jlp.pysalueid)
            WHERE jlp.verkko IN (1, 3)
            GROUP BY jp.pysalueid
            HAVING COUNT(*) > 1);
            """)

            for row in cursor:
                yield row

def get_jore4_stop_points():
    query = """query {
        service_pattern_scheduled_stop_point {
            label
            measured_location
            priority
            validity_start
            validity_end
        }
    }
    """
    headers = {'content-type': 'application/json; charset=UTF-8',
             'x-hasura-admin-secret': secret}
    response = requests.post(graphql, headers=headers, json={"query": query})
    json_data = response.json()
    if not json_data['data']:
        return {}

    stop_points = json.loads(response.content)['data']['service_pattern_scheduled_stop_point']
    result_dict = {}

    for x in stop_points:
        result_dict.setdefault(x['label'], []).append({
            'label': x['label'],
            'lat': x['measured_location']['coordinates'][1],
            'lon': x['measured_location']['coordinates'][0],
            'priority': str(x['priority']),
            'validity_start': x['validity_start'],
            'validity_end': x['validity_end']
        })

    return result_dict

def update_stop_point(label, netexid):
    mutation = """mutation UpdateStopRef(
      $netexid: String,
      $label: String) {
        update_service_pattern_scheduled_stop_point(where: {label: {_eq: $label}}, _set: {stop_place_ref: $netexid}) {
      affected_rows
      }
    }
    """
    variables = {
      "label": label,
      "netexid": netexid
    }
    headers = {'content-type': 'application/json; charset=UTF-8',
             'x-hasura-admin-secret': secret}
    response = requests.post(graphql, headers=headers, json={"query": mutation, "variables": variables})
    formatted = response.json()
    if formatted['data']:
        print(f"Scheduled stop point {label} reference updated")
    else:
        print(f"Scheduled stop point {label} reference update failed")

def mapTransportMode(verkko):
    match verkko:
        case 1:
            return 'bus'
        case 2:
            return 'metro'
        case 3:
            return 'tram'
        case 4:
            return 'rail'
        case 5:
            return 'water'
        case _:
            return 'bus'

def mapStopModel(jore3model):
    match jore3model:
        case '1':
            return 'pullOut' # syvennys
        case '2':
            return 'busBulb' # uloke
        case '4':
            return 'inLane'  # ajorata
        case _:
            return 'other'   # '3' muu

def mapStopType(jore3type):
    match jore3type:
        case '01':
            return 'glass'
        case '02':
            return 'steel'
        case '04':
            return 'post'
        case '05':
            return 'urban'
        case '06':
            return 'concrete'
        case '07':
            return 'wooden'
        case _:
            return 'virtual'

def mapStopElectricity(jore3elec):
    match jore3elec:
        case '01':
            return 'none'
        case '02':
            return 'continuous'
        case _:
            return 'light'

def mapStopCondition(jore3condition):
    match jore3condition:
        case '01':
            return 'good'
        case '02':
            return 'mediocre'
        case _:
            return 'bad'

def mapBoolean(jore3value):
    match jore3value:
        case 1:
            return True
        case _:
            return False

def toFloat(value):
    jsonval = json.dumps(value)
    if jsonval != 'null':
        return float(jsonval)
    return None

def quayInputForJore3Stop(jore3row, label, validityStart, validityEnd, lon, lat):
    return {
      "publicCode": label,
      "privateCode": {
        "value": jore3row['soltunnus'],
        "type": 'HSL/JORE-3'
      },
      "description": {
        "lang": "fin",
        "value": jore3row['pyspaikannimi']
      },
      "geometry": {
          "type": "Point",
          "coordinates": [lon, lat]
      },
      "alternativeNames": [
        {
          "name": {
            "value": jore3row['pyspaikannimir'],
            "lang": 'swe'
          },
          "nameType": "other"
        }
      ],
      "keyValues": [
        {
            "key": "stopState",
            "values": "InOperation"
        },
        {
            "key": "mainLine",
            "values": "false"
        },
        {
            "key": "virtual",
            "values": "false"
        },
        {
            "key": "postalCode",
            "values": [jore3row['postinro']]
        },
        {
            "key": "functionalArea",
            "values": [str(jore3row['pyssade'])]
        },
        {
            "key": "streetAddress",
            "values": [jore3row['pysosoite']]
        },
        {
            "key": "priority",
            "values": ['10']
        },
        {
            "key": "validityStart",
            "values": [validityStart]
        },
        {
            "key": "validityEnd",
            "values": [validityEnd]
        }
      ],
      "accessibilityAssessment": {
        "hslAccessibilityProperties": {
          "curbBackOfRailDistance": toFloat(jore3row['korotus_ajorataan']),
          "lowerCleatHeight": toFloat(jore3row['alapiena_korkeus']),
          "platformEdgeWarningArea": mapBoolean(jore3row['varoitusalue']),
          "stopAreaLengthwiseSlope": toFloat(jore3row['pituuskaltevuus']),
          "stopAreaSideSlope": toFloat(jore3row['sivukaltevuus']),
          "stopAreaSurroundingsAccessible": mapBoolean(jore3row['esteeton_kulku']),
          "stopElevationFromSidewalk": toFloat(jore3row['korotus_kaytavaan']),
          "stopType": mapStopModel(jore3row['pysakin_malli'])
        },
        "limitations": {
          "audibleSignalsAvailable": "FALSE",
          "escalatorFreeAccess": "FALSE",
          "liftFreeAccess": "FALSE",
          "stepFreeAccess": "FALSE",
          "wheelchairAccess": "FALSE"
        }
      },
      "placeEquipments": {
        "shelterEquipment": ([
          {
            "enclosed": jore3row['pysakkityyppi'] == '01' or jore3row['pysakkityyppi'] == '02',
            "shelterType": mapStopType(jore3row['pysakkityyppi']),
            "shelterElectricity": mapStopElectricity(jore3row['sahko']),
            "shelterLighting": True,
            "shelterCondition": mapStopCondition(jore3row['katos_kunto']),
            "timetableCabinets": toFloat(jore3row['kpl_lisavarusteet']) if jore3row['lisavarusteet'] == '01' else 0,
            "trashCan": mapBoolean(jore3row['roska_astia']),
            "shelterHasDisplay": mapBoolean(jore3row['nayttolaitteet']),
            "bicycleParking": jore3row['lisavarusteet'] == '03' or jore3row['lisavarusteet'] == '04',
            "leaningRail": False,
            "outsideBench": False,
            "shelterFasciaBoardTaping": False
          }
          ] * jore3row['kpl_pysakkityyppi']) if jore3row['kpl_pysakkityyppi'] else None,
        "generalSign": {
          "numberOfFrames": toFloat(jore3row['kpl_kilvet'])
        }
      }
    }

def update_stop_place(lat, lon, validityStart, validityEnd, jore3result, quayInput, transportMode):
  stopMutation2 = """
    mutation AddStopPlace(
        $privateCode: String!,
        $stopName: String!,
        $shortName: String,
        $longName: String,
        $stopNameSwe: String,
        $shortNameSwe: String,
        $longNameSwe: String,
        $validityStart: String,
        $validityEnd: String,
        $coordinates: stop_registry_Coordinates,
        $quays: [stop_registry_QuayInput],
        $transportMode: stop_registry_TransportModeType
    ) {
    stop_registry {
        mutateStopPlace(
        StopPlace: {
            geometry: { type: Point, coordinates: $coordinates },
            alternativeNames: [
                { name: {lang: "fin", value: $longName}, nameType: alias },
                { name: {lang: "swe", value: $longNameSwe}, nameType: alias },
                { name: {lang: "swe", value: $stopNameSwe}, nameType: translation }
            ],
            name: {lang: "fin", value: $stopName},
            privateCode: {value: $privateCode, type: "HSL/JORE-3"},
            keyValues: [
              { key: "validityStart", values: [$validityStart] },
              { key: "validityEnd", values: [$validityEnd] }
            ],
            quays: $quays,
            transportMode: $transportMode
        }) {
        id
        quays {
            publicCode
            id            
        }
        }
    }
    }
    """

  variables2 = {
      "privateCode": jore3result['pysalueid'],
      "stopName": jore3result['pysnimi'],
      "longName": jore3result['pysnimipitka'],
      "stopNameSwe": jore3result['pysnimir'],
      "longNameSwe": jore3result['pysnimipitkar'],
      "coordinates": [lon, lat],
      "validityStart": validityStart,
      "validityEnd": validityEnd,
      "quays": quayInput,
      "transportMode": transportMode
  }

  headers = {'content-type': 'application/json; charset=UTF-8',
                'x-hasura-admin-secret': secret}
  response = requests.post(graphql, headers=headers, json={"query": stopMutation2, "variables": variables2})

  formatted = response.json()

  if formatted['data']:
    return formatted['data']['stop_registry']['mutateStopPlace'][0]['quays']
  if formatted['errors']:
    if formatted['errors'][0]['message']:
        print(formatted['errors'][0]['message'])
    else:
        print(f"Stop place {jore3result['pysalueid']} update failed!")

  return {}

startTime = datetime.datetime.now()

j4stopPoints = get_jore4_stop_points()
print(f"Found {len(j4stopPoints)} stop points")
added = 0

j3stopPlaces = get_jore3_stops()
index = 0

for j3StopArea in get_jore3_stop_areas():
    index += 1
    print(f"Handling stop area {index}")
    quayInput = []
    latCoords = []
    lonCoords = []
    validityStarts = []
    validityEnds = []
    lastJ3Stop = None
    try:
        for j3stop in j3stopPlaces[j3StopArea['pysalueid']]:
            try:
                stopLabel = j3stop['solkirjain'] + j3stop['sollistunnus']
                if not stopLabel in j4stopPoints:
                    continue
                j4stopPoint = j4stopPoints[stopLabel][0]
                lat = j4stopPoint['lat']
                lon = j4stopPoint['lon']
                validityStart = j4stopPoint['validity_start']
                validityEnd = j4stopPoint['validity_end']
                quayInput.append(quayInputForJore3Stop(j3stop, j4stopPoint['label'], validityStart, validityEnd,
                                                       lon, lat))
                latCoords.append(lat)
                lonCoords.append(lon)
                validityStarts.append(validityStart)
                validityEnds.append(validityEnd)
                lastJ3Stop = j3stop
            except:
                print(f"Failed to handle stop {j3stop['soltunnus']}: {j3stop['pysnimi']}")
        if (len(lonCoords) > 0 and len(latCoords) > 0 and len(validityStarts) > 0 and len(validityEnds) > 0):

            # Average coordinates of quays for the stop place
            stopPlaceLon = sum(lonCoords) / len(lonCoords)
            stopPlaceLat = sum(latCoords) / len(latCoords)

            # Use min / max validity period of the stop points for the stop place
            stopPlaceValidityStart = min(validityStarts)
            stopPlaceValidityEnd = max(validityEnds)

            netexIds = update_stop_place(stopPlaceLat, stopPlaceLon, stopPlaceValidityStart, stopPlaceValidityEnd,
                                         lastJ3Stop, quayInput, mapTransportMode(j3StopArea['verkko']))
            if (netexIds):
                added += 1
                for netexAssociation in netexIds:
                    update_stop_point(netexAssociation['publicCode'], netexAssociation['id'])

    except Exception as e:
        print(f"Failed to handle stop area {j3StopArea['pysalueid']}")
        print(e)

endTime = datetime.datetime.now()
duration = endTime - startTime
print(f"Added {added} stop places")
minutes = duration.seconds // 60
print(f"Import took {minutes} minutes {duration.seconds - (minutes * 60)} seconds")
