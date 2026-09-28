package dev.raj.hostsports.kafka;

import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import dev.raj.hostsports.notification.BookingNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final BookingNotificationService notificationService;

    public KafkaConsumerService(BookingNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "booking-created", groupId = "booking-created-group")
    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {

        notificationService.sendBookingCreatedNotification(event);
    }
}