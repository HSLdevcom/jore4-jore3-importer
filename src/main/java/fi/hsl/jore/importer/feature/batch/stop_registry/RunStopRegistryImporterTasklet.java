package fi.hsl.jore.importer.feature.batch.stop_registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

/**
 * Runs the external Python "stop-registry importer" script as a Spring Batch tasklet, streaming the script's
 * stdout/stderr line-by-line through SLF4J so that its output is intermixed with the Java application's normal logs.
 */
public class RunStopRegistryImporterTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger("stop-registry-importer");
    private static final String LEGACY_TLS_OPENSSL_CONFIG = "openssl-legacy-tls.cnf";

    private final String pythonCommand;
    private final String scriptPath;
    private final String workingDir;
    private final long timeoutHours;
    private final String sourceDbUrl;
    private final String sourceDbUsername;
    private final String sourceDbPassword;

    public RunStopRegistryImporterTasklet(
            final String pythonCommand,
            final String scriptPath,
            final String workingDir,
            final long timeoutHours,
            final String sourceDbUrl,
            final String sourceDbUsername,
            final String sourceDbPassword) {
        this.pythonCommand = pythonCommand;
        this.scriptPath = scriptPath;
        this.workingDir = workingDir;
        this.timeoutHours = timeoutHours;
        this.sourceDbUrl = sourceDbUrl;
        this.sourceDbUsername = sourceDbUsername;
        this.sourceDbPassword = sourceDbPassword;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {

        LOG.info(
                "Starting stop-registry importer script: {} (cwd={}, command={})",
                scriptPath,
                workingDir,
                pythonCommand);

        final ProcessBuilder pb = new ProcessBuilder(List.of(pythonCommand, scriptPath))
                .directory(new File(workingDir))
                .redirectErrorStream(true);
        // Force unbuffered output from Python so logs appear in near real time.
        final Map<String, String> environment = pb.environment();
        environment.put("PYTHONUNBUFFERED", "1");
        environment.put("STOP_REGISTRY_IMPORTER_USE_DOTENV", "0");
        final SourceDatabaseConnection sourceDatabase = configureSourceDatabase(environment);
        configureLegacyTls(environment, sourceDatabase);

        final Process process = pb.start();

        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LOG.info(line);
            }
        }

        final boolean finished = process.waitFor(timeoutHours, TimeUnit.HOURS);
        if (!finished) {
            process.destroyForcibly();
            throw new IllegalStateException("Stop-registry importer timed out after " + timeoutHours + " hour(s)");
        }
        final int exitCode = process.exitValue();
        LOG.info("Stop-registry importer exited with code {}", exitCode);
        if (exitCode != 0) {
            throw new IllegalStateException("Stop-registry importer failed, exit code " + exitCode);
        }
        return RepeatStatus.FINISHED;
    }

    private SourceDatabaseConnection configureSourceDatabase(final Map<String, String> environment) {
        final SourceDatabaseConnection sourceDatabase = parseSourceDatabaseUrl(sourceDbUrl);
        environment.put("SOURCE_DB_HOSTNAME", sourceDatabase.hostname());
        environment.put("SOURCE_DB_PORT", sourceDatabase.port());
        environment.put("SOURCE_DB_DATABASE", sourceDatabase.database());
        environment.put("SOURCE_DB_ENCRYPTION", sourceDatabase.encryption());
        environment.put("SOURCE_DB_USERNAME", sourceDbUsername);
        environment.put("SOURCE_DB_PASSWORD", sourceDbPassword);
        return sourceDatabase;
    }

    private void configureLegacyTls(
            final Map<String, String> environment, final SourceDatabaseConnection sourceDatabase) {
        if (!"require".equals(sourceDatabase.encryption())) {
            return;
        }

        final File opensslConfig = new File(workingDir, LEGACY_TLS_OPENSSL_CONFIG).getAbsoluteFile();
        if (!opensslConfig.isFile()) {
            throw new IllegalStateException("Missing OpenSSL configuration file: " + opensslConfig);
        }
        environment.put("OPENSSL_CONF", opensslConfig.getPath());
        LOG.warn("Enabling legacy TLS compatibility for the encrypted stop-registry importer database connection.");
    }

    static SourceDatabaseConnection parseSourceDatabaseUrl(final String jdbcUrl) {
        final String prefix = "jdbc:sqlserver://";
        if (jdbcUrl == null || !jdbcUrl.regionMatches(true, 0, prefix, 0, prefix.length())) {
            throw new IllegalArgumentException("source.db.url must be a SQL Server JDBC URL");
        }

        final String connectionDetails = jdbcUrl.substring(prefix.length());
        final int propertyStart = connectionDetails.indexOf(';');
        final String server =
                (propertyStart >= 0 ? connectionDetails.substring(0, propertyStart) : connectionDetails).trim();
        final String properties = propertyStart >= 0 ? connectionDetails.substring(propertyStart + 1) : "";
        final HostAndPort hostAndPort = parseHostAndPort(server);
        final String database = parseDatabase(properties);
        final String encryption = parseEncryption(properties);
        return new SourceDatabaseConnection(hostAndPort.hostname(), hostAndPort.port(), database, encryption);
    }

    private static HostAndPort parseHostAndPort(final String server) {
        if (server.isEmpty()) {
            throw new IllegalArgumentException("source.db.url must specify a SQL Server hostname");
        }
        if (server.startsWith("[")) {
            final int closingBracket = server.indexOf(']');
            if (closingBracket < 0
                    || (closingBracket + 1 < server.length() && server.charAt(closingBracket + 1) != ':')) {
                throw new IllegalArgumentException("source.db.url has an invalid SQL Server host and port");
            }
            final String hostname = server.substring(1, closingBracket);
            final String port = closingBracket + 1 < server.length() ? server.substring(closingBracket + 2) : "1433";
            return new HostAndPort(hostname, validatePort(port));
        }

        final int colon = server.lastIndexOf(':');
        if (colon < 0) {
            return new HostAndPort(server, "1433");
        }
        return new HostAndPort(server.substring(0, colon), validatePort(server.substring(colon + 1)));
    }

    private static String validatePort(final String port) {
        try {
            final int parsedPort = Integer.parseInt(port);
            if (parsedPort < 1 || parsedPort > 65535) {
                throw new NumberFormatException();
            }
            return String.valueOf(parsedPort);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("source.db.url has an invalid SQL Server port", exception);
        }
    }

    private static String parseDatabase(final String properties) {
        for (final String property : properties.split(";")) {
            final int separator = property.indexOf('=');
            if (separator < 0) {
                continue;
            }
            final String key = property.substring(0, separator).trim();
            if ("database".equalsIgnoreCase(key) || "databaseName".equalsIgnoreCase(key)) {
                final String database = property.substring(separator + 1).trim();
                if (!database.isEmpty()) {
                    return database;
                }
            }
        }
        throw new IllegalArgumentException("source.db.url must include a non-empty database or databaseName property");
    }

    private static String parseEncryption(final String properties) {
        for (final String property : properties.split(";")) {
            final int separator = property.indexOf('=');
            if (separator < 0
                    || !"encrypt"
                            .equalsIgnoreCase(property.substring(0, separator).trim())) {
                continue;
            }
            return switch (property.substring(separator + 1).trim().toLowerCase(java.util.Locale.ROOT)) {
                case "true", "strict" -> "require";
                case "false" -> "off";
                default -> throw new IllegalArgumentException("source.db.url has an invalid encrypt property");
            };
        }

        // mssql-jdbc 13.x defaults encrypt to true; preserve that effective setting for pymssql.
        return "require";
    }

    record SourceDatabaseConnection(String hostname, String port, String database, String encryption) {}

    private record HostAndPort(String hostname, String port) {}
}
