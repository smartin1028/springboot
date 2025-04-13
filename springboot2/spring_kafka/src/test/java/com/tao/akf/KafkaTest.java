package com.tao.akf;

import com.tao.akf.service.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaTest {

    @Autowired
    private KafkaProducerService producerService;

    @Test
    void testSendAndReceive() {
        assertDoesNotThrow(() -> {
            producerService.sendMessage("Test Message");
            // 잠시 대기하여 메시지가 처리될 시간을 줌
            Thread.sleep(1000);
        });
    }
}
