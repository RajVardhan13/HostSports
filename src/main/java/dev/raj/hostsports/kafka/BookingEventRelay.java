package dev.raj.hostsports.kafka;

import dev.raj.hostsports.entity.Booking;
import dev.raj.hostsports.kafka.event.BookingCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class BookingEventRelay {

    private final KafkaProducerService producer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onBookingCreated(BookingCreatedEvent event){
        producer.sendBookingCreatedEvent(event);
    }
}
