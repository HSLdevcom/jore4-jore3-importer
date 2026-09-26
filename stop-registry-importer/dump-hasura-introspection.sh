#!/usr/bin/env bash
set -euo pipefail

# Dumps full GraphQL introspection JSON and a stop_registry-only subset.
# Defaults:
#   HASURA_URL=http://localhost:3201/v1/graphql
#   HASURA_ADMIN_SECRET=hasura
# Optional env vars:
#   OUT_FULL (default: hasura-introspection-full.json)
#   OUT_STOP_REGISTRY (default: hasura-introspection-stop-registry.json)

if ! command -v curl >/dev/null 2>&1; then
  echo "Error: curl is required" >&2
  exit 1
fi

if ! command -v jq >/dev/null 2>&1; then
  echo "Error: jq is required" >&2
  exit 1
fi

HASURA_URL="${HASURA_URL:-http://localhost:3201/v1/graphql}"
HASURA_ADMIN_SECRET="${HASURA_ADMIN_SECRET:-hasura}"

OUT_FULL="${OUT_FULL:-hasura-introspection-full.json}"
OUT_STOP_REGISTRY="${OUT_STOP_REGISTRY:-hasura-introspection-stop-registry.json}"

mkdir -p "$(dirname "$OUT_FULL")"
mkdir -p "$(dirname "$OUT_STOP_REGISTRY")"

QUERY_FILE="$(mktemp)"
PAYLOAD_FILE="$(mktemp)"
RESPONSE_FILE="$(mktemp)"
trap 'rm -f "$QUERY_FILE" "$PAYLOAD_FILE" "$RESPONSE_FILE"' EXIT

cat > "$QUERY_FILE" <<'EOF'
query IntrospectionQuery {
  __schema {
    queryType { name }
    mutationType { name }
    subscriptionType { name }
    types {
      ...FullType
    }
    directives {
      name
      description
      locations
      args {
        ...InputValue
      }
    }
  }
}

fragment FullType on __Type {
  kind
  name
  description
  fields(includeDeprecated: true) {
    name
    description
    args { ...InputValue }
    type { ...TypeRef }
    isDeprecated
    deprecationReason
  }
  inputFields { ...InputValue }
  interfaces { ...TypeRef }
  enumValues(includeDeprecated: true) {
    name
    description
    isDeprecated
    deprecationReason
  }
  possibleTypes { ...TypeRef }
}

fragment InputValue on __InputValue {
  name
  description
  type { ...TypeRef }
  defaultValue
}

fragment TypeRef on __Type {
  kind
  name
  ofType {
    kind
    name
    ofType {
      kind
      name
      ofType {
        kind
        name
        ofType {
          kind
          name
          ofType {
            kind
            name
            ofType {
              kind
              name
              ofType {
                kind
                name
              }
            }
          }
        }
      }
    }
  }
}
EOF

jq -n --rawfile q "$QUERY_FILE" '{query:$q}' > "$PAYLOAD_FILE"

curl -sS "$HASURA_URL" \
  -H "content-type: application/json" \
  -H "x-hasura-admin-secret: $HASURA_ADMIN_SECRET" \
  -d @"$PAYLOAD_FILE" \
  > "$RESPONSE_FILE"

if ! jq -e '.data.__schema' "$RESPONSE_FILE" >/dev/null 2>&1; then
  echo "Error: introspection response did not contain .data.__schema" >&2
  echo "Response excerpt:" >&2
  jq '{errors, data}' "$RESPONSE_FILE" >&2 || cat "$RESPONSE_FILE" >&2
  exit 3
fi

jq . "$RESPONSE_FILE" > "$OUT_FULL"
jq '.data.__schema.types
    | map(select(.name != null and (.name | startswith("stop_registry_"))))' "$OUT_FULL" > "$OUT_STOP_REGISTRY"

echo "Wrote full introspection to: $OUT_FULL"
echo "Wrote stop_registry subset to: $OUT_STOP_REGISTRY"
