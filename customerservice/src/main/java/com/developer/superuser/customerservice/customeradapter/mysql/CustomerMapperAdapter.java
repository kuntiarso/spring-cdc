package com.developer.superuser.customerservice.customeradapter.mysql;

import com.developer.superuser.customerservice.customer.Customer;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CustomerMapperAdapter {
    public CustomerEntity map(Customer customer) {
        return CustomerEntity.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .zipCode(customer.getZipCode())
                .country(customer.getCountry())
                .build();
    }

    public List<CustomerEntity> map(List<Customer> customers) {
        return customers.stream().map(CustomerMapperAdapter::map).toList();
    }

    public Customer map(CustomerEntity customerEntity) {
        return Customer.builder()
                .customerId(customerEntity.getCustomerId())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .email(customerEntity.getEmail())
                .phoneNumber(customerEntity.getPhoneNumber())
                .address(customerEntity.getAddress())
                .city(customerEntity.getCity())
                .state(customerEntity.getState())
                .zipCode(customerEntity.getZipCode())
                .country(customerEntity.getCountry())
                .createdAt(customerEntity.getCreatedAt())
                .updatedAt(customerEntity.getUpdatedAt())
                .createdBy(customerEntity.getCreatedBy())
                .updatedBy(customerEntity.getUpdatedBy())
                .build();
    }
}
