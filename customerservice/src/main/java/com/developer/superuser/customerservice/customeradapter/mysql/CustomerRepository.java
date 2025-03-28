package com.developer.superuser.customerservice.customeradapter.mysql;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = CustomerserviceConstants.SOFT_DELETE_RESTORE_QUERY, nativeQuery = true)
    void restoreById(@Param("id") Long id);
}
