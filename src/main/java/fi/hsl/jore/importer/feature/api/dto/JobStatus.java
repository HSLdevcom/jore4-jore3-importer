package fi.hsl.jore.importer.feature.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.util.Optional;
import org.immutables.value.Value;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;

@Value.Immutable
@JsonSerialize(as = ImmutableJobStatus.class)
@JsonDeserialize(as = ImmutableJobStatus.class)
public interface JobStatus {

    long id();

    BatchStatus batchStatus();

    String exitCode();

    Optional<String> exitDescription();

    Optional<LocalDateTime> startTime();

    Optional<LocalDateTime> endTime();

    static JobStatus from(final JobExecution execution) {
        return ImmutableJobStatus.builder()
                .id(execution.getId())
                .batchStatus(execution.getStatus())
                .exitCode(execution.getExitStatus().getExitCode())
                .exitDescription(
                        Optional.of(execution.getExitStatus().getExitDescription())
                                .filter(description -> !description.isEmpty())
                                .map(description -> description.split(System.lineSeparator())[0]))
                .startTime(Optional.ofNullable(execution.getStartTime()))
                .endTime(Optional.ofNullable(execution.getEndTime()))
                .build();
    }
}
