package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.booking.BookingRequest;
import dev.raj.hostsports.dto.booking.BookingResponse;
import dev.raj.hostsports.entity.Booking;
import dev.raj.hostsports.entity.BookingStatus;
import dev.raj.hostsports.entity.Slot;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.exception.SlotAlreadyBookedException;
import dev.raj.hostsports.mapper.BookingMapper;
import dev.raj.hostsports.repository.BookingRepository;
import dev.raj.hostsports.repository.SlotRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.service.BookingService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import dev.raj.hostsports.entity.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import org.springframework.security.access.AccessDeniedException;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SlotRepository slotRepository;
    private final UserRepository userRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request, UserDetails currentUser) {
        User player = resolveUser(currentUser);

        Slot slot = slotRepository.findByIdForUpdate(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + request.getSlotId()));

        if(!slot.isAvailable()){
            throw new SlotAlreadyBookedException("This slot has already been booked");
        }

        double hours = Duration.between(slot.getStartTime(), slot.getEndTime()).toMinutes() / 60.0;
        double totalAmount = hours * slot.getVenue().getPricePerHour();

        slot.setAvailable(false);
        slotRepository.save(slot);

        Booking booking = Booking.builder()
                .player(player)
                .slot(slot)
                .status(BookingStatus.PENDING)
                .totalAmount(totalAmount)
                .build();

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));
    }

    @Override
    public List<BookingResponse> getMyBookings(UserDetails currentUser) {
        User player = resolveUser(currentUser);
        return bookingRepository.findByPlayerOrderByBookedAtDesc(player).stream()
                .map(bookingMapper::toResponse)
                .toList();

    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(Long bookingId, UserDetails currentUser) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        User user = resolveUser(currentUser);
        boolean isOwner = booking.getPlayer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new AccessDeniedException("You do not own this booking");
        }

        if(booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.COMPLETED){
            throw new BadRequestException("Booking cannot be cancelled in its current state: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Slot slot = booking.getSlot();
        slot.setAvailable(true);
        slotRepository.save(slot);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }
}
