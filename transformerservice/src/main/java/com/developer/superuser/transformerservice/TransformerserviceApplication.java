package com.developer.superuser.transformerservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@EnableKafkaStreams
public class TransformerserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransformerserviceApplication.class, args);
	}

}
