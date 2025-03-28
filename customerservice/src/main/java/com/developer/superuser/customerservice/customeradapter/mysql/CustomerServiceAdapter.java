package com.developer.superuser.customerservice.customeradapter.mysql;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import com.developer.superuser.customerservice.customer.Customer;
import com.developer.superuser.customerservice.customer.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class CustomerServiceAdapter implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createSingle(Customer customer) {
        customerRepository.save(CustomerMapperAdapter.map(customer));
    }

    @Override
    public void createMany(List<Customer> customers) {
        customerRepository.saveAll(CustomerMapperAdapter.map(customers));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateById(Long id, Customer customer) {
        if (isExist(id)) {
            CustomerEntity entity = findCustomerEntity(id).orElseThrow(EntityNotFoundException::new);
            Map<String, Object> updatedFields = objectMapper.convertValue(customer, Map.class);
            log.info("Updating customer with update fields {}", updatedFields);
            updatedFields.forEach((key, value) -> {
                Field field;
                try {
                    field = CustomerEntity.class.getDeclaredField(key);
                    field.setAccessible(true);
                    field.set(entity, value);
                } catch (NoSuchFieldException | IllegalAccessException ex) {
                    log.error("Could not update customer field --- {}", ex.getMessage(), ex);
                    throw new IllegalArgumentException("Could not update customer field", ex);
                }
            });
            customerRepository.save(entity);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public void deleteById(Long id) {
        if (!isExist(id)) throw new EntityNotFoundException();
        customerRepository.deleteById(id);
    }

    @Override
    public void restoreById(Long id) {
        if (!isExistIncludeDeleted(id)) throw new EntityNotFoundException();
        customerRepository.restoreById(id);
    }

    @Override
    public boolean isExist(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(CustomerserviceConstants.SOFT_DELETE_FILTER);
        return customerRepository.existsById(id);
    }

    @Override
    public Customer getById(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(CustomerserviceConstants.SOFT_DELETE_FILTER);
        return findCustomerEntity(id)
                .map(CustomerMapperAdapter::map)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Page<Customer> getPageable(Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(CustomerserviceConstants.SOFT_DELETE_FILTER);
        return customerRepository.findAll(pageable).map(CustomerMapperAdapter::map);
    }

    public boolean isExistIncludeDeleted(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.disableFilter(CustomerserviceConstants.SOFT_DELETE_FILTER);
        return customerRepository.existsById(id);
    }

    private Optional<CustomerEntity> findCustomerEntity(Long id) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(CustomerserviceConstants.SOFT_DELETE_FILTER);
        return customerRepository.findById(id);
    }
}
