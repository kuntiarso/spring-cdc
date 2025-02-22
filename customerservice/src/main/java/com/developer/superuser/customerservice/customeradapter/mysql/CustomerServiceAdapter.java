package com.developer.superuser.customerservice.customeradapter.mysql;

import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customer.CustomerService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class CustomerServiceAdapter implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public void createSingle(Customer customer) {
        customerRepository.save(CustomerMapperAdapter.map(customer));
    }

    @Override
    public void createMany(List<Customer> customers) {
        customerRepository.saveAll(CustomerMapperAdapter.map(customers));
    }

    @Override
    public void updateById(Customer customer) {
        if (isExist(customer.getCustomerId())) {
            CustomerEntity entity = customerRepository.getReferenceById(customer.getCustomerId());
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            Map<String, Object> updatedFields = mapper.convertValue(entity, Map.class);
            updatedFields.forEach((key, value) -> {
                Field field = null;
                try {
                    field = CustomerEntity.class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(entity, value);
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    log.error("Could not update customer field --- {}", ex.getMessage(), ex);
                    throw new RuntimeException("Could not update customer field", ex);
                }
            });
            customerRepository.save(CustomerMapperAdapter.map(customer));
        } else {
            throw new EntityNotFoundException("Customer not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean isExist(Long id) {
        return customerRepository.existsById(id);
    }

    @Override
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .map(CustomerMapperAdapter::map)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }
}
