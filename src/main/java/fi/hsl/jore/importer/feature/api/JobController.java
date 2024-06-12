package fi.hsl.jore.importer.feature.api;

import fi.hsl.jore.importer.config.jobs.JobConfig;
import fi.hsl.jore.importer.feature.api.dto.JobStatus;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {

    private static final Logger LOG = LoggerFactory.getLogger(JobController.class);
    private static final String JOB_NAME = JobConfig.JOB_NAME;
    private static final JobParameters PARAMS = new JobParameters();

    private final Job job;
    private final JobLauncher jobLauncher;
    private final JobRepository jobRepository;

    public JobController(
            final Job job, final JobLauncher jobLauncher, final JobRepository jobRepository) {
        this.job = job;
        this.jobLauncher = jobLauncher;
        this.jobRepository = jobRepository;
    }

    private ResponseEntity<JobStatus> lastJobStatus() {
        @Nullable
        final JobExecution execution = jobRepository.getLastJobExecution(JOB_NAME, PARAMS);
        if (execution == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(JobStatus.from(execution));
    }

    @GetMapping("/job/import/status")
    public ResponseEntity<JobStatus> jobStatus() {
        return lastJobStatus();
    }

    @PostMapping("/job/import/start")
    public ResponseEntity<JobStatus> startJob() {
        try {
            final JobExecution execution = jobLauncher.run(job, PARAMS);
            LOG.info("Starting a new import job.");
            return ResponseEntity.ok(JobStatus.from(execution));
        } catch (final JobExecutionAlreadyRunningException e) {
            LOG.info("Import job was already running, returning current job state.");
            return lastJobStatus();
        } catch (final JobRestartException e) {
            // This should never happen
            LOG.warn("Attempted to restart the import job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (final JobInstanceAlreadyCompleteException e) {
            // This should never happen
            LOG.warn("Attempted to start a completed import job");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (final JobParametersInvalidException e) {
            // This should never happen
            LOG.warn("Attempted to start the import job with invalid parameters");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
