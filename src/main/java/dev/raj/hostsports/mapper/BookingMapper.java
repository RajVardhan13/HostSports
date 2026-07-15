package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.booking.BookingResponse;
import dev.raj.hostsports.entity.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    public BookingResponse toResponse(Booking booking){
        return BookingResponse.builder()
                .id(booking.getId())
                .slotId(booking.getSlot().getId())
                .venueId(booking.getSlot().getVenue().getId())
                .venueName(booking.getSlot().getVenue().getName())
                .slotStartTime(booking.getSlot().getStartTime())
                .slotEndTime(booking.getSlot().getEndTime())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .bookedAt(booking.getBookedAt())
                .build();
    }
}
