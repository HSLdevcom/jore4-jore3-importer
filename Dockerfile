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
FROM eclipse-temurin:25_36-jre

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# expose server port
EXPOSE 8080

# download script for reading docker secrets
RUN curl -o /tmp/read-secrets.sh "https://raw.githubusercontent.com/HSLdevcom/jore4-tools/main/docker/read-secrets.sh"

# copy over helper scripts
COPY ./build-jdbc-urls.sh /tmp/

# copy over compiled jar
COPY --from=builder /build/target/*.jar /usr/src/jore4-jore3-importer/importer.jar

# read docker secrets into environment variables, fetch digiroad data and run application
CMD ["/bin/bash", "-c", \
     "source /tmp/read-secrets.sh && source /tmp/build-jdbc-urls.sh && java -jar /usr/src/jore4-jore3-importer/importer.jar"]

HEALTHCHECK --interval=1m --timeout=5s \
  CMD curl --fail http://localhost:8080/actuator/health
