package com.tao.akf.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void consume(String message) {
        log.info("consume");
        log.info("Consumed message: {}", message);
    }
}

