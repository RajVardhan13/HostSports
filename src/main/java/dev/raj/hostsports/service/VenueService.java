package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.venue.VenueRequest;
import dev.raj.hostsports.dto.venue.VenueResponse;
import dev.raj.hostsports.entity.SportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface VenueService {
    VenueResponse createVenue(VenueRequest request, UserDetails currentUser);
    VenueResponse updateVenue(Long venueId, VenueRequest request, UserDetails currentUser);
    void deleteVenue(Long venueId, UserDetails currentUser);
    VenueResponse getVenueById(Long venueId);
    Page<VenueResponse> searchVenues(String city, SportType sportType, Pageable pageable);
    List<VenueResponse> getMyVenues(UserDetails currentUser);
}
