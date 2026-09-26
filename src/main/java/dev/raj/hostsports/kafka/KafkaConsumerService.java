package dev.raj.hostsports.kafka;

import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "booking-created", groupId = "booking-created-group")
    public void consumeBookingCreatedEvent(BookingCreatedEvent event) {

        System.out.println("Booking Created Event Received:");
        System.out.println("Booking ID: " + event.getBookingId());
        System.out.println("Player ID: " + event.getPlayerId());
        System.out.println("Slot ID: " + event.getSlotId());
        System.out.println("Total Amount: " + event.getTotalAmount());
    }
}