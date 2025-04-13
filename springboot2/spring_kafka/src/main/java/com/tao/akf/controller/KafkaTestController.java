package com.tao.akf.controller;

import com.tao.akf.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaProducerService producerService;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody String message) {
        producerService.sendMessage(message);
        return "Message published: " + message;
    }
}
