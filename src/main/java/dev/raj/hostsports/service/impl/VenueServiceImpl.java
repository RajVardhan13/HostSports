package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.venue.VenueRequest;
import dev.raj.hostsports.dto.venue.VenueResponse;
import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.entity.Venue;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.VenueMapper;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.repository.VenueRepository;
import dev.raj.hostsports.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final VenueMapper venueMapper;

    @Override
    @Transactional
    public VenueResponse createVenue(VenueRequest request, UserDetails currentUser) {
        User owner = resolveUser(currentUser);
        Venue venue = venueMapper.toEntity(request,owner);
        return venueMapper.toResponse(venueRepository.save(venue));
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public VenueResponse updateVenue(Long venueId, VenueRequest request, UserDetails currentUser) {
        Venue venue = findVenueOrThrow(venueId);
        User user = resolveUser(currentUser);
        assertOwnerOrAdmin(venue, user);

        venueMapper.updateEntity(venue, request);
        return venueMapper.toResponse(venueRepository.save(venue));
    }

    private void assertOwnerOrAdmin(Venue venue, User user) {
        boolean isOwner = venue.getOwner().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if (!isAdmin && !isOwner){
            throw new AccessDeniedException("You do not own this venue");
        }
    }

    private Venue findVenueOrThrow(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteVenue(Long venueId, UserDetails currentUser) {
        Venue venue = findVenueOrThrow(venueId);
        User user = resolveUser(currentUser);
        assertOwnerOrAdmin(venue, user);

        venue.setActive(false);
        venueRepository.save(venue);
    }

    @Override
    public VenueResponse getVenueById(Long venueId) {
        return venueMapper.toResponse(findVenueOrThrow(venueId));
    }

    @Override
    public Page<VenueResponse> searchVenues(String city, SportType sportType, Pageable pageable) {
        return venueRepository.search(city, sportType, pageable)
                .map(venueMapper::toResponse);
    }

    @Override
    public List<VenueResponse> getMyVenues(UserDetails currentUser) {
        User owner = resolveUser(currentUser);
        return venueRepository.findByOwner(owner).stream()
                .map(venueMapper::toResponse)
                .toList();
    }
}
