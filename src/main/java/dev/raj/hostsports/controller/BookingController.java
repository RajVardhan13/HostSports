package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.booking.BookingRequest;
import dev.raj.hostsports.dto.booking.BookingResponse;
import dev.raj.hostsports.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking", description = "Slot booking by players")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingRequest request,
                                                         @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(request, currentUser));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<BookingResponse>> getMyBooking(@AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(bookingService.getMyBookings(currentUser));
    }

    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long bookingId,
                                                         @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId,currentUser));
    }
}
