package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.slot.SlotRequest;
import dev.raj.hostsports.dto.slot.SlotResponse;
import dev.raj.hostsports.entity.Slot;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.entity.Venue;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.DuplicateResourceException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.SlotMapper;
import dev.raj.hostsports.repository.SlotRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.repository.VenueRepository;
import dev.raj.hostsports.service.SlotService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {

    private final SlotRepository slotRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final SlotMapper slotMapper;

    @Override
    @Transactional
    public SlotResponse createSlot(Long venueId, SlotRequest request, UserDetails currentUser) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));

        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isOwner = venue.getOwner().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You do not own this venue");
        }

        if(!request.getEndTime().isAfter(request.getStartTime())){
            throw new BadRequestException("Slot end time must be after start time");
        }

        if(slotRepository.existsByVenueAndStartTime(venue, request.getStartTime())){
            throw new DuplicateResourceException("A slot already exists for this venue at this start time");
        }

        Slot slot = slotMapper.toEntity(request, venue);
        return slotMapper.toResponse(slotRepository.save(slot));
    }

    @Override
    public List<SlotResponse> getAvailableSlotsForVenue(Long venueId) {
        Venue venue = venueRepository.findById(venueId)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + venueId));

        return slotRepository
                .findByVenueAndAvailableTrueAndStartTimeAfterOrderByStartTime(venue, LocalDateTime.now())
                .stream()
                .map(slotMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteSlot(Long slotId, UserDetails currentUser) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + slotId));

        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isAdmin = user.getRole().name().equals("ADMIN");
        boolean isOwner = slot.getVenue().getOwner().getId().equals(user.getId());

        if(!isOwner && !isAdmin){
            throw new AccessDeniedException("You do not own this venue");
        }

        if(!slot.isAvailable()){
            throw new BadRequestException("Cannot delete a slot that is already booked");
        }

        slotRepository.delete(slot);
    }
}
