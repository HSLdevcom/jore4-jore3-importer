#!/usr/bin/env python3

import argparse
import json
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path


class GraphqlFixture:
    def __init__(self, fixture_path):
        fixture_data = json.loads(Path(fixture_path).read_text(encoding="utf-8"))
        self.stop_points = fixture_data.get("stop_points", [])
        self.organisations = fixture_data.get("organisations", {})
        self._lock = threading.Lock()

    def upsert_organisation(self, name):
        with self._lock:
            if name not in self.organisations:
                key = name.lower().replace(" ", "-")
                self.organisations[name] = f"org-{key}"
            return self.organisations[name]


def build_response(payload, fixture):
    query = payload.get("query", "") or ""
    variables = payload.get("variables", {}) or {}

    if "service_pattern_scheduled_stop_point" in query and "query" in query:
        return {
            "data": {
                "service_pattern_scheduled_stop_point": fixture.stop_points,
            }
        }

    if "stop_registry" in query and "organisation" in query and "query" in query:
        return {
            "data": {
                "stop_registry": {
                    "organisation": [
                        {"id": org_id, "name": name}
                        for name, org_id in fixture.organisations.items()
                    ]
                }
            }
        }

    if "mutation InsertOrganisation" in query:
        name = variables.get("name", "unknown")
        org_id = fixture.upsert_organisation(name)
        return {
            "data": {
                "stop_registry": {
                    "mutateOrganisation": [
                        {
                            "id": org_id,
                            "name": name,
                        }
                    ]
                }
            }
        }

    if "mutation AddStopPlace" in query:
        quays = variables.get("quays", [])
        return {
            "data": {
                "stop_registry": {
                    "mutateStopPlace": [
                        {
                            "id": f"NSR:StopPlace:{variables.get('privateCode', 'unknown')}",
                            "quays": [
                                {
                                    "publicCode": quay.get("publicCode"),
                                    "id": f"NSR:Quay:{quay.get('publicCode')}",
                                }
                                for quay in quays
                            ],
                        }
                    ]
                }
            }
        }

    if "mutation UpdateStopRef" in query:
        return {
            "data": {
                "update_service_pattern_scheduled_stop_point": {
                    "affected_rows": 1,
                }
            }
        }

    return {"data": {}}


def make_handler(capture_path, fixture):
    class Handler(BaseHTTPRequestHandler):
        def do_POST(self):
            content_len = int(self.headers.get("content-length", 0))
            body = self.rfile.read(content_len).decode("utf-8") if content_len else "{}"
            payload = json.loads(body)

            with capture_path.open("a", encoding="utf-8") as capture_file:
                capture_file.write(json.dumps(payload) + "\n")

            response = build_response(payload, fixture)
            response_bytes = json.dumps(response).encode("utf-8")

            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.send_header("Content-Length", str(len(response_bytes)))
            self.end_headers()
            self.wfile.write(response_bytes)

        def log_message(self, fmt, *args):
            return

    return Handler


def parse_args():
    parser = argparse.ArgumentParser(description="Mock GraphQL server with request capture")
    parser.add_argument("--host", default="127.0.0.1")
    parser.add_argument("--port", type=int, default=3900)
    parser.add_argument("--fixture", required=True)
    parser.add_argument("--capture", required=True)
    return parser.parse_args()


def main():
    args = parse_args()
    capture_path = Path(args.capture)
    capture_path.parent.mkdir(parents=True, exist_ok=True)
    capture_path.write_text("", encoding="utf-8")

    fixture = GraphqlFixture(args.fixture)
    server = ThreadingHTTPServer((args.host, args.port), make_handler(capture_path, fixture))

    try:
        server.serve_forever()
    except KeyboardInterrupt:
        pass
    finally:
        server.server_close()


if __name__ == "__main__":
    main()
