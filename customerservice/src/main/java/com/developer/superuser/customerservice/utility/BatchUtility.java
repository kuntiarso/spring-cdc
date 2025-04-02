package com.developer.superuser.customerservice.utility;

import lombok.experimental.UtilityClass;
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
}
