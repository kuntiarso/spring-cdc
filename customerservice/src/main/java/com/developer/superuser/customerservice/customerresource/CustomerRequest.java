package com.developer.superuser.customerservice.customerresource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CustomerRequest {

    @NotEmpty(message = "Request must have at least one customer data to proceed")
    @Valid
    private List<Customer> customers;

    @Data
    public static class Customer {
        @NotEmpty(message = "First name is required")
        @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
        private String firstName;
        @NotEmpty(message = "Last name is required")
        @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
        private String lastName;
        @NotEmpty(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;
        @Pattern(regexp = "^[0-9]*$", message = "Phone number should only contain digits")
        @Size(max = 13, message = "Phone number must be maximum 13 digits")
        private String phoneNumber;
        private String address;
        private String city;
        private String state;
        @Pattern(regexp = "^[0-9]{5}$", message = "Zip code must be exactly 5 digits")
        private String zipCode;
        private String country;
    }
}
