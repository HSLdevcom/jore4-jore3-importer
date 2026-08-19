package fi.hsl.jore.importer.feature.batch.stop_registry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class RunStopRegistryImporterTaskletTest {

    @Test
    void translatesEncryptedJdbcConnectionToPymssqlRequireEncryption() {
        final RunStopRegistryImporterTasklet.SourceDatabaseConnection connection =
                RunStopRegistryImporterTasklet.parseSourceDatabaseUrl(
                        "jdbc:sqlserver://localhost:56239;databaseName=joretest;encrypt=true;"
                                + "trustServerCertificate=true");

        assertThat(connection.hostname()).isEqualTo("localhost");
        assertThat(connection.port()).isEqualTo("56239");
        assertThat(connection.database()).isEqualTo("joretest");
        assertThat(connection.encryption()).isEqualTo("require");
    }

    @Test
    void translatesUnencryptedJdbcConnectionToPymssqlEncryptionOff() {
        final RunStopRegistryImporterTasklet.SourceDatabaseConnection connection =
                RunStopRegistryImporterTasklet.parseSourceDatabaseUrl(
                        "jdbc:sqlserver://example.test;database=joretest;encrypt=false");

        assertThat(connection.port()).isEqualTo("1433");
        assertThat(connection.encryption()).isEqualTo("off");
    }

    @Test
    void usesCurrentJdbcDriverDefaultEncryptionWhenEncryptIsAbsent() {
        final RunStopRegistryImporterTasklet.SourceDatabaseConnection connection =
                RunStopRegistryImporterTasklet.parseSourceDatabaseUrl(
                        "jdbc:sqlserver://example.test:1444;database=joretest");

        assertThat(connection.encryption()).isEqualTo("require");
    }

    @Test
    void rejectsUnsupportedJdbcEncryptionValues() {
        assertThatThrownBy(() -> RunStopRegistryImporterTasklet.parseSourceDatabaseUrl(
                        "jdbc:sqlserver://example.test;database=joretest;encrypt=maybe"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("source.db.url has an invalid encrypt property");
    }
}
