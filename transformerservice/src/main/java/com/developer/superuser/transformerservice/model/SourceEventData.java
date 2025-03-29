package com.developer.superuser.transformerservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceEventData {
    private CustomerData before;
    private CustomerData after;
    private SourceData source;
    private String op;
    private long tsMs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerData {
        private Long customerId;
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String address;
        private String city;
        private String state;
        private String zipCode;
        private String country;
        private long createdAt;
        private long updatedAt;
        private Long deletedAt;
        private String createdBy;
        private String updatedBy;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SourceData {
        private String version;
        private String connector;
        private String name;
        private long tsMs;
        private String snapshot;
        private String db;
        private String table;
        private int serverId;
        private String gtid;
        private String file;
    }
}
