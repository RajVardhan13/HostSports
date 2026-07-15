package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.tournament.TournamentRequest;
import dev.raj.hostsports.dto.tournament.TournamentResponse;
import dev.raj.hostsports.entity.*;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.TournamentMapper;
import dev.raj.hostsports.repository.TournamentRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.repository.VenueRepository;
import dev.raj.hostsports.service.TournamentService;
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
public class TournamentServiceImpl implements TournamentService {

    private final TournamentRepository tournamentRepository;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final TournamentMapper tournamentMapper;

    @Override
    @Transactional
    public TournamentResponse createTournament(TournamentRequest request, UserDetails currentUser) {
        User organizer = resolveUser(currentUser);

        if(!request.getEndDate().isAfter(request.getStartDate())){
            throw new BadRequestException("End date must be after start date");
        }

        if(!request.getRegistrationDeadLine().isBefore(request.getStartDate())){
            throw new BadRequestException("Registration deadline must be before the tournament start date");
        }

        Venue venue = null;
        if(request.getVenueId() != null){
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: "  + request.getVenueId()));
        }

        Tournament tournament = tournamentMapper.toEntity(request, organizer, venue);
        return tournamentMapper.toResponse(tournamentRepository.save(tournament));
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public TournamentResponse updateTournament(Long tournamentId, TournamentRequest request, UserDetails currentUser) {
        Tournament tournament = findTournamentOrThrow(tournamentId);
        assertOrganizerOrAdmin(tournament, resolveUser(currentUser));

        if(tournament.getStatus() == TournamentStatus.ONGOING || tournament.getStatus() == TournamentStatus.COMPLETED){
            throw new BadRequestException("Cannot edit a tournament that is ongoing or completed");
        }

        Venue venue = null;
        if(request.getVenueId() != null){
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + request.getVenueId()));
        }

        tournament.setName(request.getName());
        tournament.setSportType(request.getSportType());
        tournament.setCity(request.getCity());
        tournament.setRegistrationDeadLine(request.getRegistrationDeadLine());
        tournament.setStartDate(request.getStartDate());
        tournament.setEndDate(request.getEndDate());
        tournament.setMaxTeams(request.getMaxTeams());
        tournament.setDescription(request.getDescription());
        tournament.setVenue(venue);

        return tournamentMapper.toResponse(tournamentRepository.save(tournament));
    }

    private void assertOrganizerOrAdmin(Tournament tournament, User user) {
        boolean isOrganizer = tournament.getOrganizer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if(!isOrganizer && !isAdmin){
            throw new AccessDeniedException("You do not organize the tournament");
        }
    }

    private Tournament findTournamentOrThrow(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + id));
    }

    @Override
    public TournamentResponse getTournamentById(Long tournamentId) {
        return tournamentMapper.toResponse(findTournamentOrThrow(tournamentId));
    }

    @Override
    public Page<TournamentResponse> searchTournaments(String city, SportType sportType,
                                                      TournamentStatus status, Pageable pageable) {
        return tournamentRepository.search(city,sportType,status,pageable).map(tournamentMapper :: toResponse);
    }

    @Override
    public List<TournamentResponse> getMyTournaments(UserDetails currentUser) {
        User organizer = resolveUser(currentUser);
        return tournamentRepository.findByOrganizer(organizer).stream().map(tournamentMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public TournamentResponse cancelTournament(Long tournamentId, UserDetails currentUser) {
        Tournament tournament = findTournamentOrThrow(tournamentId);
        assertOrganizerOrAdmin(tournament,resolveUser(currentUser));

        if(tournament.getStatus() == TournamentStatus.COMPLETED){
            throw new BadRequestException("Cannot cancel a completed tournament");
        }

        tournament.setStatus(TournamentStatus.CANCELLED);
        return tournamentMapper.toResponse(tournamentRepository.save(tournamentRepository.save(tournament)));
    }
}
