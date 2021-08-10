CREATE DATABASE testsourcedb;
GO
-- Jore uses Microsoft SQL Server 2012 (SP4) (KB4018073) - 11.0.7001.0 (X64),
-- so let's set the  compatibility for SQL Server 2012 (11.x) => 110
-- See: https://docs.microsoft.com/en-us/sql/t-sql/statements/alter-database-transact-sql-compatibility-level
ALTER DATABASE testsourcedb SET COMPATIBILITY_LEVEL = 110
GO
USE testsourcedb;
GO
--
-- Create jr_solmu
--
CREATE TABLE jr_solmu
(
    soltunnus    CHAR(7) NOT NULL,
    soltyyppi    CHAR    NOT NULL,
    sollistunnus VARCHAR(4),
    solmapiste   VARCHAR,
    solx         NUMERIC(7),
    soly         NUMERIC(7),
    solmx        NUMERIC(8, 6),
    solmy        NUMERIC(8, 6),
    solkuka      VARCHAR(20),
    solviimpvm   DATETIME2(3),
    solstx       NUMERIC(7),
    solsty       NUMERIC(7),
    solx3        NUMERIC(7),
    soly3        NUMERIC(7),
    solstx3      NUMERIC(7),
    solsty3      NUMERIC(7),
    solstmx      NUMERIC(8, 6),
    solstmy      NUMERIC(8, 6),
    solkirjain   VARCHAR(2),
    solhis       VARCHAR,
    solox        NUMERIC(7),
    soloy        NUMERIC(7),
    solomx       NUMERIC(8, 6),
    solomy       NUMERIC(8, 6),
    solotapa     VARCHAR,
    mittpvm      DATETIME2(3),
    mkjmx        NUMERIC(8, 6),
    mkjmy        NUMERIC(8, 6)
)
GO
CREATE UNIQUE CLUSTERED INDEX jr_solmu_cind ON jr_solmu (soltunnus)
GO
--
-- Create jr_linkki
--
CREATE TABLE jr_linkki
(
    lnkverkko     CHAR    NOT NULL,
    lnkalkusolmu  CHAR(7) NOT NULL,
    lnkloppusolmu CHAR(7) NOT NULL,
    lnkmitpituus  INT,
    lnkpituus     INT,
    lnkstid       INT,
    katkunta      CHAR(3),
    katnimi       VARCHAR(40),
    kaoosnro      SMALLINT,
    lnksuunta     CHAR,
    lnkosnro      SMALLINT,
    lnkostrk      VARCHAR,
    lnkkuka       VARCHAR(20),
    lnkviimpvm    DATETIME2(3),
    lnkhis        VARCHAR
)
GO
CREATE UNIQUE CLUSTERED INDEX jr_linkki_cind ON jr_linkki (lnkverkko, lnkalkusolmu, lnkloppusolmu)
GO
--
-- Create jr_piste
--
CREATE TABLE jr_piste
(
    lnkverkko     CHAR    NOT NULL,
    lnkalkusolmu  CHAR(7) NOT NULL,
    lnkloppusolmu CHAR(7) NOT NULL,
    pisjarjnro    INT     NOT NULL,
    pisid         INT     NOT NULL,
    pisx          NUMERIC(7),
    pisy          NUMERIC(7),
    pismx         NUMERIC(8, 6),
    pismy         NUMERIC(8, 6),
    piskuka       VARCHAR(20),
    pisviimpvm    DATETIME2(3)
)
GO
CREATE CLUSTERED INDEX jr_piste_cind ON jr_piste (lnkverkko, lnkalkusolmu, lnkloppusolmu, pisjarjnro)
GO
CREATE UNIQUE INDEX jr_piste_uind ON jr_piste (pisid)
GO
--
-- Create jr_linja
--
CREATE TABLE jr_linja
(
    lintunnus      VARCHAR(6) NOT NULL,
    linperusreitti VARCHAR(6),
    linvoimast     DATETIME2(3),
    linvoimviimpvm DATETIME2(3),
    linjoukkollaji VARCHAR(2),
    lintilorg      VARCHAR(3),
    linverkko      VARCHAR,
    linryhma       VARCHAR(3),
    linkuka        VARCHAR(20),
    linviimpvm     DATETIME2(3),
    linjlkohde     VARCHAR(6),
    id             VARCHAR(4),
    vaihtoaika     INT,
    linkorvtyyppi  VARCHAR(2),
    puhelinnumero  VARCHAR(20)
)
GO
CREATE UNIQUE CLUSTERED INDEX jr_linja_cind ON jr_linja (lintunnus)
GO
