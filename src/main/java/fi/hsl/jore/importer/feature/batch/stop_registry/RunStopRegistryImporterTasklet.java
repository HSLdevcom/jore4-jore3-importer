package fi.hsl.jore.importer.feature.batch.stop_registry;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;

/**
 * Runs the external Python "stop-registry importer" script as a Spring Batch tasklet, streaming
 * the script's stdout/stderr line-by-line through SLF4J so that its output is intermixed with the
 * Java application's normal logs.
 */
public class RunStopRegistryImporterTasklet implements Tasklet {

    private static final Logger LOG = LoggerFactory.getLogger("stop-registry-importer");

    private final String scriptPath;
    private final String workingDir;
    private final long timeoutHours;

    public RunStopRegistryImporterTasklet(
            final String scriptPath, final String workingDir, final long timeoutHours) {
        this.scriptPath = scriptPath;
        this.workingDir = workingDir;
        this.timeoutHours = timeoutHours;
    }

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext)
            throws Exception {

        LOG.info("Starting stop-registry importer script: {} (cwd={})", scriptPath, workingDir);

        final ProcessBuilder pb = new ProcessBuilder(List.of("python3", "-u", scriptPath))
                .directory(new File(workingDir))
                .redirectErrorStream(true);
        // Force unbuffered output from Python so logs appear in near real time.
        pb.environment().put("PYTHONUNBUFFERED", "1");

        final Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                LOG.info(line);
            }
        }

        final boolean finished = process.waitFor(timeoutHours, TimeUnit.HOURS);
        if (!finished) {
            process.destroyForcibly();
            throw new IllegalStateException(
                    "Stop-registry importer timed out after " + timeoutHours + " hour(s)");
        }
        final int exitCode = process.exitValue();
        LOG.info("Stop-registry importer exited with code {}", exitCode);
        if (exitCode != 0) {
            throw new IllegalStateException(
                    "Stop-registry importer failed, exit code " + exitCode);
        }
        return RepeatStatus.FINISHED;
    }
}


