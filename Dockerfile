# builder docker image
FROM maven:3-eclipse-temurin-25 AS builder

# set up workdir
WORKDIR /build

# download dependencies
COPY ./pom.xml /build
RUN mvn de.qaware.maven:go-offline-maven-plugin:resolve-dependencies

# build
COPY ./src /build/src
COPY ./profiles/prod /build/profiles/prod
RUN mvn clean package spring-boot:repackage -Pprod

# distributed docker image
FROM eclipse-temurin:25.0.2_10-jre

# Application Insights version
ARG APPINSIGHTS_VERSION=3.7.7

# expose server port
EXPOSE 8080

# download script for reading Docker secrets
ADD --chmod=555 https://raw.githubusercontent.com/HSLdevcom/jore4-tools/main/docker/read-secrets.sh /tmp/read-secrets.sh

# Downaload a Java applet to perform HEALTHCHECK with
ADD --chmod=444 https://raw.githubusercontent.com/HSLdevcom/jore4-tools/main/docker/HealthCheck.jar /app/scripts/HealthCheck.jar

# Connection string is provided as env in Kubernetes by secrets manager
# it should not be provided for other environments (local etc)
ADD --chmod=444 https://github.com/microsoft/ApplicationInsights-Java/releases/download/${APPINSIGHTS_VERSION}/applicationinsights-agent-${APPINSIGHTS_VERSION}.jar /usr/src/jore4-jore3-importer/applicationinsights-agent.jar
COPY --chmod=444 ./applicationinsights.json /usr/src/jore4-jore3-importer/applicationinsights.json

# add helper script for constructing JDBC URL
COPY --chmod=555 ./build-jdbc-urls.sh /tmp/

# copy over compiled jar
COPY --from=builder /build/target/*.jar /usr/src/jore4-jore3-importer/importer.jar

# read docker secrets into environment variables, fetch digiroad data and run application
CMD ["/bin/bash", "-c", \
     "source /tmp/read-secrets.sh && source /tmp/build-jdbc-urls.sh && java -javaagent:/usr/src/jore4-jore3-importer/applicationinsights-agent.jar -jar /usr/src/jore4-jore3-importer/importer.jar"]

HEALTHCHECK --interval=1m --timeout=5s \
  CMD ["java", "-jar", "/app/scripts/HealthCheck.jar"]
