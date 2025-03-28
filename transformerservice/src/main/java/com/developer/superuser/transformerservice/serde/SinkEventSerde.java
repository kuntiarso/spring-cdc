package com.developer.superuser.transformerservice.serde;

import com.developer.superuser.transformerservice.model.SinkEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class SinkEventSerde extends Serdes.WrapperSerde<SinkEventData> {
    public SinkEventSerde(ObjectMapper objectMapper) {
        super(new SinkEventDataSerializer(objectMapper), new SinkEventDataDeserializer(objectMapper));
    }
}

@RequiredArgsConstructor
@Slf4j
class SinkEventDataSerializer implements Serializer<SinkEventData> {
    private final ObjectMapper objectMapper;

    @Override
    public byte[] serialize(String s, SinkEventData sinkEventData) {
        try {
            log.info("Serializing key: {} with SinkEventData: {}", s, sinkEventData);
            return objectMapper.writeValueAsBytes(sinkEventData);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize sink event data", ex);
            throw new IllegalStateException("Failed to serialize sink event data");
        }
    }
}

@RequiredArgsConstructor
@Slf4j
class SinkEventDataDeserializer implements Deserializer<SinkEventData> {
    private final ObjectMapper objectMapper;

    @Override
    public SinkEventData deserialize(String s, byte[] bytes) {
        try {
            if (bytes == null || bytes.length == 0) return null;
            log.info("Deserializing key: {} with SinkEventData: {}", s, bytes);
            return objectMapper.readValue(bytes, SinkEventData.class);
        } catch (Exception ex) {
            log.error("Failed to deserialize sink event data", ex);
            throw new IllegalArgumentException("Failed to deserialize sink event data");
        }
    }
}
