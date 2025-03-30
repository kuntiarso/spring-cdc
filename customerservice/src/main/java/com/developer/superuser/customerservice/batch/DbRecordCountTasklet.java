package com.developer.superuser.customerservice.batch;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
@Slf4j
public class DbRecordCountTasklet implements Tasklet {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Integer count = jdbcTemplate.queryForObject(CustomerserviceConstants.BATCH_RECORD_COUNT_QUERY, Integer.class);
        log.info("{} customer records in db", count);
        return RepeatStatus.FINISHED;
    }
}
