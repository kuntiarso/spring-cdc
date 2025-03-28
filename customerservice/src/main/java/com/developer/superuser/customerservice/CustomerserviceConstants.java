package com.developer.superuser.customerservice;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CustomerserviceConstants {
    public final String CUSTOMER_ENTITY = "\"customer\"";
    public final String SOFT_DELETE_QUERY = "UPDATE customer SET deleted_at = NOW() WHERE customer_id = ?";
    public final String SOFT_DELETE_RESTORE_QUERY = "UPDATE customer SET deleted_at = NULL WHERE customer_id = :id";
    public final String SOFT_DELETE_FILTER = "softDeleteFilter";
    public final String SOFT_DELETE_FILTER_QUERY = "deleted_at IS NULL";
}
