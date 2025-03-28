package com.developer.superuser.transformerservice.serde;

import com.developer.superuser.transformerservice.model.SourceEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class SourceEventSerde extends Serdes.WrapperSerde<SourceEventData> {
    public SourceEventSerde(ObjectMapper objectMapper) {
        super(new SourceEventDataSerializer(objectMapper), new SourceEventDataDeserializer(objectMapper));
    }
}

@RequiredArgsConstructor
@Slf4j
class SourceEventDataSerializer implements Serializer<SourceEventData> {
    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String s, SourceEventData sourceEventData) {
        try {
            log.info("Serializing key: {} with SourceEventData: {}", s, sourceEventData);
            return objectMapper.writeValueAsBytes(sourceEventData);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize source event data", ex);
            throw new IllegalStateException("Failed to serialize source event data");
        }
    }
}

@RequiredArgsConstructor
@Slf4j
class SourceEventDataDeserializer implements Deserializer<SourceEventData> {
    private final ObjectMapper objectMapper;

    @Override
    public SourceEventData deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null || bytes.length == 0) return null;
            log.info("Deserializing key: {} with SourceEventData: {}", s, bytes);
            return objectMapper.readValue(bytes, SourceEventData.class);
        } catch (Exception ex) {
            log.error("Failed to deserialize source event data", ex);
            throw new IllegalArgumentException("Failed to deserialize source event data");
        }
    }
}
