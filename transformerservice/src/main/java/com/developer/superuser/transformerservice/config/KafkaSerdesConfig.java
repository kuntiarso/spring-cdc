package com.developer.superuser.transformerservice.config;

import com.developer.superuser.transformerservice.model.SinkEventData;
import com.developer.superuser.transformerservice.model.SourceEventData;
import com.developer.superuser.transformerservice.serde.SinkEventSerde;
import com.developer.superuser.transformerservice.serde.SourceEventSerde;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serde;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaSerdesConfig {
    @Bean
    public Serde<SourceEventData> sourceEventSerde(ObjectMapper objectMapper) {
        return new SourceEventSerde(objectMapper);
    }

    @Bean
    public Serde<SinkEventData> sinkEventSerde(ObjectMapper objectMapper) {
        return new SinkEventSerde(objectMapper);
    }
}
