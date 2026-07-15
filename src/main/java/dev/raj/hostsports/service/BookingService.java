package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.booking.BookingRequest;
import dev.raj.hostsports.dto.booking.BookingResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest request, UserDetails currentUser);
    List<BookingResponse> getMyBookings(UserDetails currentUser);
    BookingResponse cancelBooking(Long bookingId, UserDetails currentUser);
}
