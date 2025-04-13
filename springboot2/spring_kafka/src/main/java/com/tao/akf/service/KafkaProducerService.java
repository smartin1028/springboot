package com.tao.akf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private static final String TOPIC = "test-topic";

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        System.out.println("Producing message: " + message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}
