package dev.raj.hostsports.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "test-topic", groupId = "hostsports-group")
    public void consumeMessage(String message) {

        System.out.println("Received from Kafka: " + message);
    }
}