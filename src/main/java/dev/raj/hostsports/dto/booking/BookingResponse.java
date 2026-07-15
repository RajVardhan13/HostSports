package dev.raj.hostsports.dto.booking;

import dev.raj.hostsports.entity.Booking;
import dev.raj.hostsports.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private Long slotId;
    private Long venueId;
    private String venueName;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;
    private BookingStatus status;
    private Double totalAmount;
    private LocalDateTime bookedAt;
}
