package com.developer.superuser.customerservice;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerserviceConstants {
    public final String CUSTOMER_ENTITY = "\"customer\"";
    public final String SOFT_DELETE_QUERY = "UPDATE customer SET deleted_at = NOW() WHERE customer_id = ?";
    public final String SOFT_DELETE_RESTORE_QUERY = "UPDATE customer SET deleted_at = NULL WHERE customer_id = :id";
    public final String SOFT_DELETE_FILTER = "softDeleteFilter";
    public final String SOFT_DELETE_FILTER_QUERY = "deleted_at IS NULL";
    public final String BATCH_INSERT_QUERY = "INSERT INTO customer (first_name, last_name, email, phone_number, address, city, state, zip_code, country, created_by, updated_by) VALUES (:firstName, :lastName, :email, :phoneNumber, :address, :city, :state, :zipCode, :country, :createdBy, :updatedBy) ON DUPLICATE KEY UPDATE first_name = :firstName, last_name = :lastName";
    public final String BATCH_RECORD_COUNT_QUERY = "SELECT COUNT(*) FROM customer";
    public final String JOB_PARAMETER_JOB_ID = "jobId";
    public final String JOB_PARAMETER_TEMP_FILE_PATH = "tempFilePath";
    public final String JOB_PARAMETER_OUTPUT_FILE_PATH = "outputFilePath";
    public final String JOB_PARAMETER_CHUNK_SIZE = "chunkSize";
    public final String JOB_PARAMETER_TOTAL_RECORDS = "totalRecords";
}
