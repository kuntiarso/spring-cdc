package com.developer.superuser.customerservice.customerresource;

import com.developer.superuser.customerservice.v1_0_0.customerresource.CustomerHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> update(@PathVariable("customerId") @NotBlank String customerId, @Valid @RequestBody CustomerRequest.Customer customerRequest) {
        return customerHandler.updateCustomer(customerId, customerRequest);
    }

    @GetMapping("{customerId}")
    public ResponseEntity<?> getOne(@PathVariable("customerId") @NotBlank String customerId) {
        return customerHandler.getCustomer(customerId);
    }

    @DeleteMapping("{customerId}")
    public ResponseEntity<?> delete(@PathVariable("customerId") @NotBlank String customerId) {
        return customerHandler.deleteCustomer(customerId);
    }
}
