package com.developer.superuser.customerservice.batch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Getter
@Slf4j
public class StepExecutionMonitor implements StepExecutionListener {
    private int chunkSize;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Job parameters before step execution: {}", stepExecution.getJobExecution().getJobParameters());
        this.chunkSize = Optional.ofNullable(stepExecution.getJobExecution().getJobParameters().getString("chunkSize"))
                .map(Integer::parseInt)
                .orElse(10);
    }
}
