package com.developer.superuser.customerservice.config;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import com.developer.superuser.customerservice.batch.*;
import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class BatchConfig {
    @Bean
    @StepScope
    public FlatFileItemReader<CustomerRequest.Customer> reader(@Value("#{jobParameters['tempFilePath']}") String tempFilePath) {
        log.info("reader tempFilePath: {}", tempFilePath);
        return new FlatFileItemReaderBuilder<CustomerRequest.Customer>()
                .name("customerItemReader")
                .resource(new FileSystemResource(tempFilePath))
                .delimited()
                .names("firstName", "lastName", "email", "phoneNumber", "address", "city", "state", "zipCode", "country")
                .targetType(CustomerRequest.Customer.class)
                .linesToSkip(1)
                .build();
    }

    @Bean
    public ItemProcessor<CustomerRequest.Customer, Customer> processor() {
        return customerRequest -> Customer.builder()
                .firstName(customerRequest.getFirstName())
                .lastName(customerRequest.getLastName())
                .email(customerRequest.getEmail())
                .phoneNumber(customerRequest.getPhoneNumber())
                .address(customerRequest.getAddress())
                .city(customerRequest.getCity())
                .state(customerRequest.getState())
                .zipCode(customerRequest.getZipCode())
                .country(customerRequest.getCountry())
                .createdBy("Superuser")
                .updatedBy("Superuser")
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Customer> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql(CustomerserviceConstants.BATCH_INSERT_QUERY)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    @JobScope
    public Step insertCustomerStep(JobRepository jobRepository,
                                   StepExecutionMonitor stepExecutionMonitor,
                                   PlatformTransactionManager transactionManager,
                                   FlatFileItemReader<CustomerRequest.Customer> reader,
                                   ItemProcessor<CustomerRequest.Customer, Customer> processor,
                                   JdbcBatchItemWriter<Customer> writer,
                                   @Value("#{jobParameters['chunkSize']}") String chunkSize) {
        log.info("insertCustomerStep chunkSize: {}", chunkSize);
        return new StepBuilder("insertCustomerStep", jobRepository)
                .listener(stepExecutionMonitor)
                .<CustomerRequest.Customer, Customer>chunk(Integer.parseInt(chunkSize), transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step dbConnectionCheckStep(JobRepository jobRepository, DataSource dataSource, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dbConnectionCheckStep", jobRepository)
                .tasklet(new DbConnectionCheckTasklet(dataSource), transactionManager)
                .build();
    }

    @Bean
    public Step dbRecordCountStep(JobRepository jobRepository, JdbcTemplate jdbcTemplate, PlatformTransactionManager transactionManager) {
        return new StepBuilder("dbRecordCountStep", jobRepository)
                .tasklet(new DbRecordCountTasklet(jdbcTemplate), transactionManager)
                .build();
    }

    @Bean
    public Step fileCleanupStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("fileCleanupStep", jobRepository)
                .tasklet(new FileCleanupTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job customerBatchJob(JobRepository jobRepository, JobExecutionMonitor jobExecutionMonitor, Step dbConnectionCheckStep, Step insertCustomerStep, Step dbRecordCountStep, Step fileCleanupStep) {
        return new JobBuilder("customerBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionMonitor)
                .start(dbConnectionCheckStep)
                .next(insertCustomerStep)
                .next(dbRecordCountStep)
                .next(fileCleanupStep)
                .build();
    }
}
