package fi.hsl.jore.importer.feature.api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import tools.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.api.dto.ImmutableJobStatus;
import fi.hsl.jore.importer.feature.api.dto.JobStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobInstance;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class JobControllerTest {

    @Mock
    private Job job;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private JobRepository jobRepository;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private JobController jobController;

    @BeforeEach
    public void setup() {
        jobController = new JobController(job, jobLauncher, jobRepository);
        mockMvc = MockMvcBuilders.standaloneSetup(jobController).build();
    }

    @Test
    public void whenNoJobRun_thenReturnNoContent() throws Exception {
        mockMvc.perform(get("/job/import/status")).andExpect(status().isNoContent());

        verify(jobRepository, times(1)).getLastJobExecution(anyString(), any());

        verify(jobLauncher, never()).run(any(), any());
    }

    @Test
    public void whenJobRun_thenReturnLastStatus() throws Exception {
        final long id = 1L;
        final LocalDateTime startTime = LocalDateTime.of(2021, 4, 6, 14, 33, 0);
        final LocalDateTime endTime = LocalDateTime.of(2021, 4, 6, 14, 33, 12);
        final JobInstance jobInstance = new JobInstance(id, "testJob");
        final JobExecution execution = new JobExecution(id, jobInstance, new JobParameters());

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        execution.setStartTime(startTime);
        execution.setEndTime(endTime);

        when(jobRepository.getLastJobExecution(anyString(), any())).thenReturn(execution);

        final MvcResult result = mockMvc.perform(get("/job/import/status"))
                .andExpect(status().isOk())
                .andReturn();

        verify(jobRepository, times(1)).getLastJobExecution(anyString(), any());

        verify(jobLauncher, never()).run(any(), any());

        assertThat(result.getResponse().getContentType(), is(MediaType.APPLICATION_JSON_VALUE));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(
                status,
                is(ImmutableJobStatus.builder()
                        .id(id)
                        .batchStatus(BatchStatus.COMPLETED)
                        .exitCode("COMPLETED")
                        .startTime(startTime)
                        .endTime(endTime)
                        .build()));
    }

    @Test
    public void whenJobFailed_thenReturnLastStatus() throws Exception {
        final long id = 2L;
        final LocalDateTime startTime = LocalDateTime.of(2021, 4, 6, 14, 33);
        final LocalDateTime endTime = LocalDateTime.of(2021, 4, 6, 14, 33, 12);
        final JobInstance jobInstance = new JobInstance(id, "testJob");
        final JobExecution execution = new JobExecution(id, jobInstance, new JobParameters());

        final Throwable error = new RuntimeException("Guru mediation error!");

        execution.setStatus(BatchStatus.FAILED);
        execution.setExitStatus(ExitStatus.FAILED.addExitDescription(error));
        execution.setStartTime(startTime);
        execution.setEndTime(endTime);

        when(jobRepository.getLastJobExecution(anyString(), any())).thenReturn(execution);

        final MvcResult result = mockMvc.perform(get("/job/import/status"))
                .andExpect(status().isOk())
                .andReturn();

        verify(jobRepository, times(1)).getLastJobExecution(anyString(), any());

        verify(jobLauncher, never()).run(any(), any());

        assertThat(result.getResponse().getContentType(), is(MediaType.APPLICATION_JSON_VALUE));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(
                status,
                is(ImmutableJobStatus.builder()
                        .id(id)
                        .batchStatus(BatchStatus.FAILED)
                        .exitCode("FAILED")
                        .startTime(startTime)
                        .endTime(endTime)
                        .exitDescription("java.lang.RuntimeException: Guru mediation error!")
                        .build()));
    }

    @Test
    public void whenStartingJob_thenReturnInitialStatus() throws Exception {
        final long id = 3L;
        final JobInstance jobInstance = new JobInstance(id, "testJob");
        final JobExecution execution = new JobExecution(id, jobInstance, new JobParameters());

        execution.setStatus(BatchStatus.STARTING);
        execution.setExitStatus(ExitStatus.UNKNOWN);

        when(jobLauncher.run(any(), any())).thenReturn(execution);

        final MvcResult result = mockMvc.perform(post("/job/import/start"))
                .andExpect(status().isOk())
                .andReturn();

        verify(jobRepository, never()).getLastJobExecution(anyString(), any());

        verify(jobLauncher, times(1)).run(any(), any());

        assertThat(result.getResponse().getContentType(), is(MediaType.APPLICATION_JSON_VALUE));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(
                status,
                is(ImmutableJobStatus.builder()
                        .id(id)
                        .batchStatus(BatchStatus.STARTING)
                        .exitCode("UNKNOWN")
                        .build()));
    }

    @Test
    public void whenStartingJobWhileRunning_thenReturnCurrentStatus() throws Exception {
        final long id = 4L;
        final LocalDateTime startTime = LocalDateTime.of(2021, 4, 6, 14, 33);
        final JobInstance jobInstance = new JobInstance(id, "testJob");
        final JobExecution execution = new JobExecution(id, jobInstance, new JobParameters());

        execution.setStatus(BatchStatus.STARTED);
        execution.setExitStatus(ExitStatus.UNKNOWN);
        execution.setStartTime(startTime);

        when(jobLauncher.run(any(), any()))
                .thenThrow(new JobExecutionAlreadyRunningException("It's already running.."));

        when(jobRepository.getLastJobExecution(anyString(), any())).thenReturn(execution);

        final MvcResult result = mockMvc.perform(post("/job/import/start"))
                .andExpect(status().isOk())
                .andReturn();

        verify(jobRepository, times(1)).getLastJobExecution(anyString(), any());

        verify(jobLauncher, times(1)).run(any(), any());

        assertThat(result.getResponse().getContentType(), is(MediaType.APPLICATION_JSON_VALUE));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(
                status,
                is(ImmutableJobStatus.builder()
                        .id(id)
                        .batchStatus(BatchStatus.STARTED)
                        .exitCode("UNKNOWN")
                        .startTime(startTime)
                        .build()));
    }
}
