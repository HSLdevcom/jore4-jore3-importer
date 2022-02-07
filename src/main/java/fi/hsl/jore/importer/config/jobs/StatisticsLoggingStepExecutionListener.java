package fi.hsl.jore.importer.config.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * Writes step statistics to a log file after a step has been
 * finished.
 */
public class StatisticsLoggingStepExecutionListener implements StepExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsLoggingStepExecutionListener.class);

    @Override
    public void beforeStep(final StepExecution stepExecution) {
        //Left empty on purpose
    }

    @Override
    public ExitStatus afterStep(final StepExecution stepExecution) {
        LOGGER.debug("=========================================================");
        LOGGER.debug("Statistics for the step: {}", stepExecution.getStepName());
        LOGGER.debug("---------------------------------------------------------");
        LOGGER.debug("readCount: {}", stepExecution.getReadCount());
        LOGGER.debug("readSkipCount: {}", stepExecution.getReadSkipCount());
        LOGGER.debug("processSkipCount: {}", stepExecution.getProcessSkipCount());
        LOGGER.debug("writeCount: {}", stepExecution.getWriteCount());
        LOGGER.debug("writeSkipCount: {}", stepExecution.getWriteSkipCount());
        LOGGER.debug("filterCount: {}", stepExecution.getFilterCount());
        LOGGER.debug("commitCount: {}", stepExecution.getCommitCount());
        LOGGER.debug("rollbackCount: {}", stepExecution.getRollbackCount());
        LOGGER.debug("exitStatus: {}", stepExecution.getExitStatus());
        LOGGER.debug("=========================================================");
        return stepExecution.getExitStatus();
    }
}
