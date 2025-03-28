package com.developer.superuser.customerservice.config;

import com.developer.superuser.customerservice.customer.CustomerService;
import com.developer.superuser.customerservice.customeradapter.DefaultCustomerServiceAdapter;
import com.developer.superuser.customerservice.customeradapter.mysql.CustomerRepository;
import com.developer.superuser.customerservice.customeradapter.mysql.CustomerServiceAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomerAdapterConfiguration {
    @Bean
    public CustomerService customerService(CustomerRepository customerRepository, ObjectMapper objectMapper) {
        return new CustomerServiceAdapter(customerRepository, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(CustomerService.class)
    public CustomerService defaultCustomerService() {
        return new DefaultCustomerServiceAdapter();
    }
}
