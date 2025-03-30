package com.developer.superuser.customerservice.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
public class DbConnectionCheckTasklet implements Tasklet {
    private final DataSource dataSource;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        try (Connection connection = dataSource.getConnection()) {
            log.info("Database connection established: {}", connection.getCatalog());
        } catch (SQLException ex) {
            log.error("Database connection failed: {}", ex.getMessage());
            throw ex;
        }
        return RepeatStatus.FINISHED;
    }
}
