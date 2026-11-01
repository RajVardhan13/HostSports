package dev.raj.hostsports.notification;

import dev.raj.hostsports.entity.Notification;
import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import dev.raj.hostsports.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingNotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void sendBookingCreatedNotification(BookingCreatedEvent event) {
        String eventKey = "BOOKING_CREATED:" + event.getBookingId();

        if (notificationRepository.existsByEventKey(eventKey)) {
            log.info("Notification already saved for {}", eventKey);
            return;
        }

        Notification notification = new Notification();
        notification.setUserId(event.getPlayerId());
        notification.setType("BOOKING_CREATED");
        notification.setEventKey(eventKey);
        notification.setMessage("Your booking #" + event.getBookingId()
                + " is confirmed. Amount: " + event.getTotalAmount());

        notificationRepository.save(notification);
        log.info("Saved notification for booking {}", event.getBookingId());
    }
}