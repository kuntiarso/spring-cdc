package com.developer.superuser.customerservice.customeradapter;

import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customer.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class DefaultCustomerServiceAdapter implements CustomerService {
    @Override
    public void createSingle(Customer customer) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void createMany(List<Customer> customers) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void updateById(Long id, Customer customer) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void deleteById(Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public void restoreById(Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public boolean isExist(Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Customer getById(Long id) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }

    @Override
    public Page<Customer> getPageable(Pageable pageable) {
        throw new ResponseStatusException(HttpStatus.NOT_IMPLEMENTED);
    }
}
