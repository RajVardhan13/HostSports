package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.slot.SlotRequest;
import dev.raj.hostsports.dto.slot.SlotResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface SlotService {
    SlotResponse createSlot(Long venueId, SlotRequest request, UserDetails currentUser);
    List<SlotResponse> getAvailableSlotsForVenue(Long venueId);
    void deleteSlot(Long slotId, UserDetails currentUser);
}
