package com.developer.superuser.customerservice.customeradapter.mysql;

import com.developer.superuser.customerservice.CustomerserviceConstants;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Table(name = CustomerserviceConstants.CUSTOMER_ENTITY)
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = CustomerserviceConstants.SOFT_DELETE_QUERY)
@FilterDef(name = CustomerserviceConstants.SOFT_DELETE_FILTER)
@Filter(name = CustomerserviceConstants.SOFT_DELETE_FILTER, condition = CustomerserviceConstants.SOFT_DELETE_FILTER_QUERY)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "customer_id")
    private Long customerId;

    @NonNull
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NonNull
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @NonNull
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "zip_code", length = 20)
    private String zipCode;

    @Column(name = "country", length = 100)
    private String country;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedBy
    @Column(name = "created_by", length = 100)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}
