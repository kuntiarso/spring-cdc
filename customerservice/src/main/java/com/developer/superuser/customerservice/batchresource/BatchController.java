package com.developer.superuser.customerservice.batchresource;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import com.developer.superuser.customerservice.utility.BatchUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class BatchController {
    private final JobLauncher jobLauncher;
    private final Job customerBatchJob;
    private final Job generateCsvJob;

    @PostMapping("run")
    public ResponseEntity<String> runBatchJob(@RequestParam("file") MultipartFile file,
                                              @RequestParam(name = CustomerserviceConstants.JOB_PARAMETER_CHUNK_SIZE, defaultValue = "10") String chunkSize) {
        try {
            String jobId = UUID.randomUUID().toString();
            String tempFilePath = BatchUtility.saveTempFile(file, jobId);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(CustomerserviceConstants.JOB_PARAMETER_JOB_ID, jobId)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_CHUNK_SIZE, chunkSize)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_TEMP_FILE_PATH, tempFilePath)
                    .toJobParameters();
            JobExecution jobExecution = jobLauncher.run(customerBatchJob, jobParameters);
            return ResponseEntity.ok("Executing batch job with id: " + jobExecution.getId());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("An error occurred while running db insertion batch job: " + ex.getMessage());
        }
    }

    @PostMapping("mock")
    public ResponseEntity<String> generateMockJob(@RequestParam(name = CustomerserviceConstants.JOB_PARAMETER_TOTAL_RECORDS, defaultValue = "1000") String totalRecords,
                                                  @RequestParam(name = CustomerserviceConstants.JOB_PARAMETER_CHUNK_SIZE, defaultValue = "10") String chunkSize) {
        try {
            String jobId = UUID.randomUUID().toString();
            String outputFilePath = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + "mock_data_" + jobId + ".csv";
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString(CustomerserviceConstants.JOB_PARAMETER_JOB_ID, jobId)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_TOTAL_RECORDS, totalRecords)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_CHUNK_SIZE, chunkSize)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_OUTPUT_FILE_PATH, outputFilePath)
                    .toJobParameters();
            jobLauncher.run(generateCsvJob, jobParameters);
            return ResponseEntity.ok("Executing csv generation job with output file path: " + outputFilePath);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("An error occurred while running csv generation job: " + ex.getMessage());
        }
    }
}
