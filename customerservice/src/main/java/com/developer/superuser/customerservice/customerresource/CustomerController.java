package com.developer.superuser.customerservice.customerresource;

import com.developer.superuser.customerservice.v1_0_0.customerresource.CustomerHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerHandler customerHandler;

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CustomerRequest customerRequest) {
        return customerHandler.createCustomer(customerRequest);
    }

    @PutMapping("{customerId}")
    public ResponseEntity<?> update(@PathVariable("customerId") @NotBlank String customerId, @RequestBody CustomerRequest.Customer customerRequest) {
        return customerHandler.updateCustomer(customerId, customerRequest);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<?> delete(@PathVariable("customerId") @NotBlank String customerId) {
        return customerHandler.deleteCustomer(customerId);
    }

    @PutMapping("{customerId}/restore")
    public ResponseEntity<?> restore(@PathVariable("customerId") @NotBlank String customerId) {
        return customerHandler.restoreCustomer(customerId);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<?> getOne(@PathVariable("customerId") @NotBlank String customerId) {
        return customerHandler.getCustomer(customerId);
    }

    @GetMapping("page")
    public ResponseEntity<?> getPage(@PageableDefault(sort = "customerId", direction = Sort.Direction.ASC) Pageable pageable) {
        return customerHandler.getCustomerPageable(pageable);
    }
}
