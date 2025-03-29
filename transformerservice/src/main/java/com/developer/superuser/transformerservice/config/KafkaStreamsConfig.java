package com.developer.superuser.transformerservice.config;

import com.developer.superuser.transformerservice.model.SinkEventData;
import com.developer.superuser.transformerservice.model.SourceEventData;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@Slf4j
public class KafkaStreamsConfig {
    private static final String SOURCE_TOPIC = "mysql.cdc.customer";
    private static final String DESTINATION_TOPIC = "elastic.cdc.customer";

    @Bean
    public KStream<String, SourceEventData> kStream(StreamsBuilder streamsBuilder, Serde<SourceEventData> sourceEventSerde, Serde<SinkEventData> sinkEventSerde) {
        KStream<String, SourceEventData> sourceStream = streamsBuilder.stream(SOURCE_TOPIC, Consumed.with(Serdes.String(), sourceEventSerde));
        KStream<String, SinkEventData> enrichedStream = sourceStream.map(KafkaStreamsConfig::processStreamData).filter((k, v) -> Objects.nonNull(k));
        enrichedStream.to(DESTINATION_TOPIC, Produced.with(Serdes.String(), sinkEventSerde));
        return sourceStream;
    }

    private static KeyValue<String, SinkEventData> processStreamData(String key, SourceEventData source) {
        try {
            log.info("Stream key: {} and value: {}", key, source);
            if (source == null) {
                return new KeyValue<>(null, null);
            }

            String newKey = null;
            if ("d".equals(source.getOp())) {
                log.info("Processing delete operation");
                if (Objects.nonNull(source.getBefore()) && Objects.nonNull(source.getBefore().getCustomerId())) {
                    newKey = source.getBefore().getCustomerId().toString();
                }
                return new KeyValue<>(newKey, null);
            }

            log.info("Processing insert or update operation");
            if (Objects.nonNull(source.getAfter()) && Objects.nonNull(source.getAfter().getCustomerId())) {
                newKey = source.getAfter().getCustomerId().toString();
            }

            SinkEventData sinkEventData = mapSourceToSink(source);
            log.info("Data after processing, new key: {} and new value: {}", newKey, sinkEventData);
            return new KeyValue<>(newKey, sinkEventData);
        } catch (Exception ex) {
            log.error("Error while processing data", ex);
        }
        return new KeyValue<>(null, null);
    }

    private static SinkEventData mapSourceToSink(SourceEventData source) {
        return SinkEventData.builder()
                .customerId(source.getAfter().getCustomerId())
                .firstName(source.getAfter().getFirstName())
                .lastName(source.getAfter().getLastName())
                .email(source.getAfter().getEmail())
                .phoneNumber(source.getAfter().getPhoneNumber())
                .address(source.getAfter().getAddress())
                .city(source.getAfter().getCity())
                .state(source.getAfter().getState())
                .zipCode(source.getAfter().getZipCode())
                .country(source.getAfter().getCountry())
                .createdAt(source.getAfter().getCreatedAt())
                .updatedAt(source.getAfter().getUpdatedAt())
                .deletedAt(source.getAfter().getDeletedAt())
                .createdBy(source.getAfter().getCreatedBy())
                .updatedBy(source.getAfter().getUpdatedBy())
                .metadata(SinkEventData.Metadata.builder()
                        .isDeleted(Objects.nonNull(source.getAfter().getDeletedAt()))
                        .sourceTable(source.getSource().getTable())
                        .sourceDatabase(source.getSource().getDb())
                        .sourcePosition(source.getSource().getFile())
                        .connector(source.getSource().getConnector())
                        .connectorVersion(source.getSource().getVersion())
                        .eventTimestamp(source.getSource().getTsMs())
                        .ingestionTimestamp(source.getTsMs())
                        .processingTimestamp(System.currentTimeMillis())
                        .build())
                .build();
    }
}
