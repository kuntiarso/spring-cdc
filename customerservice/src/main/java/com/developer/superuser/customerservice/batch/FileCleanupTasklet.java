package com.developer.superuser.customerservice.batch;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class FileCleanupTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        try {
            String tempFilePath = chunkContext.getStepContext().getStepExecution().getJobParameters().getString(CustomerserviceConstants.JOB_PARAMETER_TEMP_FILE_PATH);
            Files.deleteIfExists(Paths.get(tempFilePath));
            log.info("Temp file {} deleted successfully", tempFilePath);
        } catch (IOException ex) {
            log.error("Error occurred while deleting temp file", ex);
        }
        return RepeatStatus.FINISHED;
    }
}
