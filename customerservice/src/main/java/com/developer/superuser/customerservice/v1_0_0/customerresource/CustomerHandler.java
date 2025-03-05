package com.developer.superuser.customerservice.v1_0_0.customerresource;

import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customer.CustomerService;
import com.developer.superuser.customerservice.customerresource.CustomerRequest;
import com.developer.superuser.customerservice.customerresource.CustomerResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerHandler {
    private final CustomerService customerService;

    public ResponseEntity<Void> createCustomer(CustomerRequest customerRequest) {
        try {
            if (customerRequest.getCustomers().size() == 1) {
                log.info("Creating a single customer");
                customerService.createSingle(CustomerMapperResource.map(customerRequest.getCustomers().getFirst()));
            } else {
                log.info("Creating multiple customers");
                customerService.createMany(CustomerMapperResource.map(customerRequest.getCustomers()));
            }
            log.info("Successfully created customer(s)");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception ex) {
            log.error("Error has happened while creating customer --- ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    public ResponseEntity<Void> updateCustomer(String customerId, CustomerRequest.Customer customerRequest) {
        try {
            log.info("Checking if customer with id {} exists", customerId);
            Validate.isTrue(customerService.isExist(Long.parseLong(customerId)), "Customer with id %s does not exist", customerId);
            log.info("Updating customer with id {}", customerId);
            Customer customer = CustomerMapperResource.map(customerRequest).toBuilder().customerId(Long.parseLong(customerId)).build();
            customerService.updateById(customer);
            log.info("Successfully updated customer with id {}", customerId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Error has happened while updating customer --- ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    public ResponseEntity<CustomerResponse> getCustomer(String customerId) {
        try {
            log.info("Retrieving customer with id --- {}", customerId);
            CustomerResponse result = CustomerMapperResource.map(customerService.getById(Long.parseLong(customerId)));
            log.info("Successfully retrieved customer with id --- {}", customerId);
            return ResponseEntity.ok().body(result);
        } catch (Exception ex) {
            log.error("Error has happened while getting customer --- ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

    public ResponseEntity<Void> deleteCustomer(String customerId) {
        try {
            log.info("Deleting customer with id --- {}", customerId);
            customerService.deleteById(Long.parseLong(customerId));
            log.info("Successfully deleted customer with id --- {}", customerId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            log.error("Error has happened while deleting customer --- ", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }
}
