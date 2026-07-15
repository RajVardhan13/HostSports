package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.slot.SlotRequest;
import dev.raj.hostsports.dto.slot.SlotResponse;
import dev.raj.hostsports.service.SlotService;
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
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Slots", description = "Venue time-slot management")
public class SlotController {

    private final SlotService slotService;

    @PostMapping("/venues/{venueId}/slots")
    public ResponseEntity<SlotResponse> createSlot(@PathVariable Long venueId,
                                                   @Valid @RequestBody SlotRequest request,
                                                   @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(slotService.createSlot(venueId, request, currentUser));
    }

    @GetMapping("/venues/{venueId}/slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(@PathVariable Long venueId){
        return ResponseEntity.ok(slotService.getAvailableSlotsForVenue(venueId));
    }

    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId,
                                           @AuthenticationPrincipal UserDetails currentUser){
        slotService.deleteSlot(slotId,currentUser);
        return ResponseEntity.noContent().build();
    }
}
