package com.developer.superuser.customerservice.customer;

import java.util.List;

public interface CustomerService {
    void createSingle(Customer customer);
    void createMany(List<Customer> customers);
    void updateById(Customer customer);
    void deleteById(Long id);
    boolean isExist(Long id);
    Customer getById(Long id);
}
