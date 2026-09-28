package dev.raj.hostsports.notification;

import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class BookingNotificationService {

    public void sendBookingCreatedNotification(BookingCreatedEvent event) {

        System.out.println("Notification: Booking " + event.getBookingId() +
                        " was created for player " + event.getPlayerId()
        );
    }
}
