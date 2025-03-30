package com.developer.superuser.customerservice.config;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import com.developer.superuser.customerservice.batch.DbConnectionCheckTasklet;
import com.developer.superuser.customerservice.batch.DbRecordCountTasklet;
import com.developer.superuser.customerservice.batch.FileCleanupTasklet;
import com.developer.superuser.customerservice.batch.JobCompletionListener;
import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
@EnableBatchProcessing
@RequiredArgsConstructor
public class BatchConfig {
    private final DataSource dataSource;
    private final JobRepository jobRepository;
    private final JdbcTemplate jdbcTemplate;
    private final PlatformTransactionManager transactionManager;

    private String tempFilePath;

    @Value("#{jobParameters['tempFilePath']}")
    public void setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<CustomerRequest.Customer> reader() {
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
    public JdbcBatchItemWriter<Customer> writer() {
        return new JdbcBatchItemWriterBuilder<Customer>()
                .sql(CustomerserviceConstants.BATCH_INSERT_QUERY)
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Step insertCustomerStep() {
        return new StepBuilder("insertCustomerStep", jobRepository)
                .<CustomerRequest.Customer, Customer>chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Step dbConnectionCheckStep() {
        return new StepBuilder("dbConnectionCheckStep", jobRepository)
                .tasklet(new DbConnectionCheckTasklet(dataSource), transactionManager)
                .build();
    }

    @Bean
    public Step dbRecordCountStep() {
        return new StepBuilder("dbRecordCountStep", jobRepository)
                .tasklet(new DbRecordCountTasklet(jdbcTemplate), transactionManager)
                .build();
    }

    @Bean
    public Step fileCleanupStep() {
        return new StepBuilder("fileCleanupStep", jobRepository)
                .tasklet(new FileCleanupTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job customerBatchJob() {
        return new JobBuilder("customerBatchJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(new JobCompletionListener())
                .start(dbConnectionCheckStep())
                .next(insertCustomerStep())
                .next(dbRecordCountStep())
                .next(fileCleanupStep())
                .build();
    }
}
