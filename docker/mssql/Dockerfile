FROM mcr.microsoft.com/mssql/server:2017-CU22-ubuntu-16.04

RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

EXPOSE 1433

# The username is "sa"
ENV SA_PASSWORD testSOURCEdb123
ENV ACCEPT_EULA Y
ENV MSSQL_PID Developer

COPY . /usr/src/app

CMD /bin/bash ./entrypoint.sh
