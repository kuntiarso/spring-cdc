package com.developer.superuser.customerservice.v1_0_0.customerresource;

import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import com.developer.superuser.customerservice.customerresource.CustomerResponse;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class CustomerMapperResource {
    public Customer map(CustomerRequest.Customer customer) {
        return Customer.builder()
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

    public List<Customer> map(List<CustomerRequest.Customer> customers) {
        return customers.stream().map(CustomerMapperResource::map).toList();
    }

    public CustomerResponse map(Customer customer) {
        return CustomerResponse.builder()
                .customerId(String.valueOf(customer.getCustomerId()))
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .zipCode(customer.getZipCode())
                .country(customer.getCountry())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .createdBy(customer.getCreatedBy())
                .updatedBy(customer.getUpdatedBy())
                .build();
    }
}
