package dev.raj.hostsports.kafka;

import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private static final String TOPIC = "booking-created";

    private final KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, BookingCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookingCreatedEvent(BookingCreatedEvent event) {
        String key = String.valueOf(event.getBookingId());

        kafkaTemplate.send(TOPIC, key, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish booking {} to Kafka", event.getBookingId(), ex);
                    } else {
                        log.info("Published booking {} to partition {} offset {}",
                                event.getBookingId(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}