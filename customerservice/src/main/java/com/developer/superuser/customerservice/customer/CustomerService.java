package com.developer.superuser.customerservice.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    void createSingle(Customer customer);
    void createMany(List<Customer> customers);
    void updateById(Long id, Customer customer);
    void deleteById(Long id);
    void restoreById(Long id);
    boolean isExist(Long id);
    Customer getById(Long id);
    Page<Customer> getPageable(Pageable pageable);
}
