package com.developer.superuser.customerservice.batchresource;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import com.developer.superuser.customerservice.utility.BatchUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("batch")
@RequiredArgsConstructor
public class BatchController {
    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;
    private final Job customerBatchJob;

    @PostMapping("run")
    public ResponseEntity<String> runBatchJob(@RequestParam("file") MultipartFile file) {
        try {
            String jobId = UUID.randomUUID().toString();
            String tempFilePath = BatchUtility.saveTempFile(file, jobId);
            BatchUtility.updateReader(tempFilePath, applicationContext);
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobId", jobId)
                    .addString(CustomerserviceConstants.JOB_PARAMETER_TEMP_FILE_PATH, tempFilePath)
                    .toJobParameters();
            JobExecution execution = jobLauncher.run(customerBatchJob, jobParameters);
            return ResponseEntity.ok("Executing batch job with id: " + execution.getId());
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("An error occurred while running batch job: " + ex.getMessage());
        }
    }
}
