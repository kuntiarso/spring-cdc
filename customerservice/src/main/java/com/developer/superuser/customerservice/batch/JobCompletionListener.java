package com.developer.superuser.customerservice.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@RequiredArgsConstructor
@Slf4j
public class JobCompletionListener implements JobExecutionListener {
    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(jobExecution.getStatus() == BatchStatus.COMPLETED ? "Job completed" : "Job failed");
    }
}
