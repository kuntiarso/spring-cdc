package com.developer.superuser.transformerservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SinkEventData {
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
    private String createdBy;
    private String updatedBy;
    @JsonProperty("_metadata")
    private Metadata metadata;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private String sourceTable;
        private String sourceDatabase;
        private String sourcePosition;
        private String connector;
        private String connectorVersion;
        private long eventTimestamp;
        private long ingestionTimestamp;
        private long processingTimestamp;
    }
}
