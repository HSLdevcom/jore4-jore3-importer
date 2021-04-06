package fi.hsl.jore.importer.feature.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.hsl.jore.importer.feature.api.dto.ImmutableJobStatus;
import fi.hsl.jore.importer.feature.api.dto.JobStatus;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;
import java.util.Date;

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

@WebMvcTest(controllers = JobController.class)
public class JobControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private Job job;

    @MockBean
    private JobLauncher jobLauncher;

    @MockBean
    private JobRepository jobRepository;

    public JobControllerTest(@Autowired final MockMvc mockMvc,
                             @Autowired final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void whenNoJobRun_thenReturnNoContent() throws Exception {
        mockMvc.perform(get("/job/import/status"))
               .andExpect(status().isNoContent());

        verify(jobRepository, times(1))
                .getLastJobExecution(anyString(), any());

        verify(jobLauncher, never())
                .run(any(), any());
    }

    @Test
    public void whenJobRun_thenReturnLastStatus() throws Exception {
        final Instant startTime = Instant.parse("2021-04-06T14:33:00.00Z");
        final Instant endTime = Instant.parse("2021-04-06T14:33:12.00Z");
        final JobExecution execution = new JobExecution(1L);

        execution.setStatus(BatchStatus.COMPLETED);
        execution.setExitStatus(ExitStatus.COMPLETED);
        execution.setStartTime(Date.from(startTime));
        execution.setEndTime(Date.from(endTime));

        when(jobRepository.getLastJobExecution(anyString(), any()))
                .thenReturn(execution);

        final MvcResult result =
                mockMvc.perform(get("/job/import/status"))
                       .andExpect(status().isOk())
                       .andReturn();

        verify(jobRepository, times(1))
                .getLastJobExecution(anyString(), any());

        verify(jobLauncher, never())
                .run(any(), any());

        assertThat(result.getResponse().getContentType(),
                   is("application/json"));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(status,
                   is(ImmutableJobStatus.builder()
                                        .batchStatus(BatchStatus.COMPLETED)
                                        .exitCode("COMPLETED")
                                        .startTime(startTime)
                                        .endTime(endTime)
                                        .build()));
    }

    @Test
    public void whenStartingJob_thenReturnInitialStatus() throws Exception {
        final JobExecution execution = new JobExecution(1L);

        execution.setStatus(BatchStatus.STARTING);
        execution.setExitStatus(ExitStatus.UNKNOWN);

        when(jobLauncher.run(any(), any()))
                .thenReturn(execution);

        final MvcResult result =
                mockMvc.perform(post("/job/import/start"))
                       .andExpect(status().isOk())
                       .andReturn();

        verify(jobRepository, never())
                .getLastJobExecution(anyString(), any());

        verify(jobLauncher, times(1))
                .run(any(), any());

        assertThat(result.getResponse().getContentType(),
                   is("application/json"));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(status,
                   is(ImmutableJobStatus.builder()
                                        .batchStatus(BatchStatus.STARTING)
                                        .exitCode("UNKNOWN")
                                        .build()));
    }

    @Test
    public void whenStartingJobWhileRunning_thenReturnCurrentStatus() throws Exception {
        final Instant startTime = Instant.parse("2021-04-06T14:33:00.00Z");
        final JobExecution execution = new JobExecution(1L);

        execution.setStatus(BatchStatus.STARTED);
        execution.setExitStatus(ExitStatus.UNKNOWN);
        execution.setStartTime(Date.from(startTime));

        when(jobLauncher.run(any(), any()))
                .thenThrow(new JobExecutionAlreadyRunningException("It's already running.."));

        when(jobRepository.getLastJobExecution(anyString(), any()))
                .thenReturn(execution);

        final MvcResult result =
                mockMvc.perform(post("/job/import/start"))
                       .andExpect(status().isOk())
                       .andReturn();

        verify(jobRepository, times(1))
                .getLastJobExecution(anyString(), any());

        verify(jobLauncher, times(1))
                .run(any(), any());

        assertThat(result.getResponse().getContentType(),
                   is("application/json"));

        final String body = result.getResponse().getContentAsString();

        final JobStatus status = objectMapper.readValue(body, JobStatus.class);

        assertThat(status,
                   is(ImmutableJobStatus.builder()
                                        .batchStatus(BatchStatus.STARTED)
                                        .exitCode("UNKNOWN")
                                        .startTime(startTime)
                                        .build()));
    }
}
