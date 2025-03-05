package com.developer.superuser.transformerservice.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {
    @Value("${spring.kafka.cdc.application-id}")
    private String applicationId;
    @Value("${spring.kafka.cdc.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${spring.kafka.cdc.source.topic}")
    private String sourceTopic;
    @Value("${spring.kafka.cdc.target.topic}")
    private String targetTopic;

    private static final ObjectMapper MAP = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamsConfig.class);

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, String> kStream(StreamsBuilder builder) {
        KStream<String, String> stream = builder.stream(sourceTopic, Consumed.with(Serdes.String(), Serdes.String()));
        stream.mapValues(this::process).filter((k, v) -> Objects.nonNull(v)).to(targetTopic, Produced.with(null, null));
        return stream;
    }

    private String process(String data) {
        try {
            LOG.info("Stream data: {}", data);
            JsonNode node = MAP.readTree(data);
            LOG.info("Pretty print in json: {}", node.toPrettyString());

            if (hasPayload(node)) {
                String operation = hasProperty(node, "op") ? getProperty(node, "op").asText() : "";
                LOG.info("CDC operation: {}", operation);
                JsonNode afterData = hasProperty(node, "after") ? getProperty(node, "after") : null;
                LOG.info("After data: {}", afterData);
                JsonNode beforeData = hasProperty(node, "before") ? getProperty(node, "before") : null;
                LOG.info("Before data: {}", beforeData);

                Map<String, Object> customer = new HashMap<>();
                boolean isDelete = operation.equals("d");

                if (!isDelete && Objects.nonNull(afterData)) {
                    customer.put("customer_id", afterData.get("customer_id").asLong());
                    customer.put("first_name", afterData.get("first_name").asText());
                    customer.put("last_name", afterData.get("last_name").asText());
                    customer.put("email", afterData.get("email").asText());
                    customer.put("phone_number", afterData.get("phone_number").asText());
                    customer.put("address", afterData.get("address").asText());
                    customer.put("city", afterData.get("city").asText());
                    customer.put("state", afterData.get("state").asText());
                    customer.put("zip_code", afterData.get("zip_code").asText());
                    customer.put("country", afterData.get("country").asText());
                    customer.put("created_at", afterData.get("created_at").asLong());
                    customer.put("updated_at", afterData.get("updated_at").asLong());
                    customer.put("created_by", afterData.get("created_by").asText());
                    customer.put("updated_by", afterData.get("updated_by").asText());
                }

                Map<String, Object> metadata = new HashMap<>();
                if (Objects.nonNull(afterData)) {
                    metadata.put("_id", afterData.get("customer_id").asLong());
                } else if (Objects.nonNull(beforeData)) {
                    metadata.put("_id", beforeData.get("customer_id").asLong());
                } else {
                    LOG.error("Stream data not contain customer_id");
                    return null;
                }
                metadata.put("_index", "customers");
                metadata.put("_deleted", isDelete);
                customer.put("@metadata", metadata);

                LOG.info("Processed customer: {}", customer);
                return MAP.writeValueAsString(customer);
            }
        } catch (Exception ex) {
            LOG.error("Error while processing stream data", ex);
            ex.fillInStackTrace();
        }
        return null;
    }

    private boolean hasPayload(JsonNode node) {
        return node.isObject() && node.has("payload");
    }

    private boolean hasProperty(JsonNode node, String key) {
        return node.get("payload").has(key);
    }

    private JsonNode getProperty(JsonNode node, String key) {
        return node.get("payload").get(key);
    }
}
