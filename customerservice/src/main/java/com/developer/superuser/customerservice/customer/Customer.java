package com.developer.superuser.customerservice.customer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Customer {
    @EqualsAndHashCode.Include
    private Long customerId;
    private String firstName;
    private String lastName;
    @EqualsAndHashCode.Include
    private String email;
    @EqualsAndHashCode.Include
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}
