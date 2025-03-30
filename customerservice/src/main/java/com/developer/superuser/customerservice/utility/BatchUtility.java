package com.developer.superuser.customerservice.utility;

import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import lombok.experimental.UtilityClass;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class BatchUtility {
    public String saveTempFile(MultipartFile file, String jobId) throws IOException {
        String tempFilePath = System.getProperty("java.io.tmpdir") + File.separator + jobId + ".csv";
        file.transferTo(new File(tempFilePath));
        return tempFilePath;
    }

    @SuppressWarnings("unchecked")
    public void updateReader(String tempFilePath, ApplicationContext applicationContext) {
        FlatFileItemReader<CustomerRequest.Customer> reader = (FlatFileItemReader<CustomerRequest.Customer>) applicationContext.getBean("reader");
        reader.setResource(new FileSystemResource(tempFilePath));
    }
}
