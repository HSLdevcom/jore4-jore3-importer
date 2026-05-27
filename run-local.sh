#!/usr/bin/env bash

set -euo pipefail

JORE_IMPORTER_MIGRATE=true \
  mvn clean spring-boot:run -Dspring-boot.run.jvmArguments="-Xmx4096m -Djdk.tls.client.protocols=TLSv1,TLSv1.1,TLSv1.2 -Djava.security.properties=java.security.override"

