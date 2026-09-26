package dev.raj.hostsports.kafka;

import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingCreatedEvent(BookingCreatedEvent event) {
        kafkaTemplate.send("booking-created", event);
    }
}