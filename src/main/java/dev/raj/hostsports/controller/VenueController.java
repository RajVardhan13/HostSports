package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.venue.VenueRequest;
import dev.raj.hostsports.dto.venue.VenueResponse;
import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.service.VenueService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues")
@RequiredArgsConstructor
@Tag(name = "Venues", description = "Venue listing and managment")
public class VenueController {

    private final VenueService venueService;

    @PostMapping
    public ResponseEntity<VenueResponse> createVenue(@Valid @RequestBody VenueRequest request,
                                                     @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(venueService.createVenue(request,currentUser));
    }

    @PutMapping("/{venueId}")
    public ResponseEntity<VenueResponse> updateVenue(@PathVariable Long venueId,
                                                     @Valid @RequestBody VenueRequest request,
                                                     @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(venueService.updateVenue(venueId, request, currentUser));
    }

    @DeleteMapping("/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Long venueId,
                                            @AuthenticationPrincipal UserDetails currentUser){
        venueService.deleteVenue(venueId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<VenueResponse>> searchVenues(
            @RequestParam(required = false) String city,
            @RequestParam(required = false)SportType sportType,
            Pageable pageable){
        return ResponseEntity.ok(venueService.searchVenues(city,sportType,pageable));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<VenueResponse>> getMyVenues(@AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(venueService.getMyVenues(currentUser));
    }
}
