package com.developer.superuser.customerservice.config;

import com.developer.superuser.customerservice.batch.CsvJobExecutionMonitor;
import com.developer.superuser.customerservice.batch.CsvStepExecutionMonitor;
import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class CsvBatchConfig {
    @Bean
    @StepScope
    public ItemReader<CustomerRequest.Customer> csvReader(@Value("#{jobParameters['totalRecords']}") String totalRecords) {
        log.info("csvReader totalRecords: {}", totalRecords);
        return new ItemReader<>() {
            private int count = 0;
            private final Faker faker = new Faker();

            @Override
            public CustomerRequest.Customer read() throws UnexpectedInputException, ParseException, NonTransientResourceException {
                if (count >= Long.parseLong(totalRecords)) return null;
                count++;
                return CustomerRequest.Customer.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .email(faker.internet().emailAddress())
                        .phoneNumber(faker.numerify("+## ### ### ###").replaceAll("[+\\s]", ""))
                        .address(faker.address().streetAddress())
                        .city(faker.address().city())
                        .state(faker.address().state())
                        .zipCode(faker.address().zipCode())
                        .country(faker.address().country().replaceAll(",", ""))
                        .build();
            }
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<CustomerRequest.Customer> csvWriter(@Value("#{jobParameters['outputFilePath']}") String outputFilePath) {
        log.info("csvWriter outputFilePath: {}", outputFilePath);
        return new FlatFileItemWriterBuilder<CustomerRequest.Customer>()
                .name("csvWriter")
                .resource(new FileSystemResource(outputFilePath))
                .delimited()
                .names("firstName", "lastName", "email", "phoneNumber", "address", "city", "state", "zipCode", "country")
                .headerCallback(writer -> writer.write("firstName,lastName,email,phoneNumber,address,city,state,zipCode,country"))
                .build();
    }

    @Bean
    @JobScope
    public Step generateCsvStep(JobRepository jobRepository,
                                CsvStepExecutionMonitor csvStepExecutionMonitor,
                                PlatformTransactionManager transactionManager,
                                ItemReader<CustomerRequest.Customer> csvReader,
                                ItemWriter<CustomerRequest.Customer> csvWriter,
                                @Value("#{jobParameters['chunkSize']}") String chunkSize) {
        log.info("generateCsvStep chunkSize: {}", chunkSize);
        return new StepBuilder("generateCsvStep", jobRepository)
                .listener(csvStepExecutionMonitor)
                .<CustomerRequest.Customer, CustomerRequest.Customer>chunk(Integer.parseInt(chunkSize), transactionManager)
                .reader(csvReader)
                .writer(csvWriter)
                .build();
    }

    @Bean
    public Job generateCsvJob(JobRepository jobRepository, CsvJobExecutionMonitor csvJobExecutionMonitor, Step generateCsvStep) {
        return new JobBuilder("generateCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(csvJobExecutionMonitor)
                .start(generateCsvStep)
                .build();
    }
}
