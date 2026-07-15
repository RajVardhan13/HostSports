package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.registration.TeamRegistrationRequest;
import dev.raj.hostsports.dto.registration.TeamRegistrationResponse;
import dev.raj.hostsports.entity.*;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.DuplicateResourceException;
import dev.raj.hostsports.exception.RegistrationClosedException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.TeamRegistrationMapper;
import dev.raj.hostsports.repository.TeamRegistrationRepository;
import dev.raj.hostsports.repository.TeamRepository;
import dev.raj.hostsports.repository.TournamentRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.service.TeamRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamRegistrationServiceImpl implements TeamRegistrationService {

    private final TeamRegistrationRepository registrationRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamRegistrationMapper registrationMapper;

    @Override
    @Transactional
    public TeamRegistrationResponse registerTeam(Long tournamentId, TeamRegistrationRequest request, UserDetails currentUser) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not find with id: " + tournamentId));

        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.getTeamId()));

        User user = resolveUser(currentUser);
        boolean isCaptain = team.getCaptain().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if(!isCaptain && !isAdmin) {
            throw new AccessDeniedException("Only the team captain can register this team");
        }

        if(LocalDateTime.now().isAfter(tournament.getRegistrationDeadLine())){
            throw new RegistrationClosedException("Registration deadline has passed for this tournament");
        }

        if(tournament.getStatus() == TournamentStatus.CANCELLED){
            throw new RegistrationClosedException("This tournament has been cancelled");
        }

        if(registrationRepository.findByTournamentAndTeam(tournament, team).isPresent()){
            throw new DuplicateResourceException("This team has already applied to this tournament");
        }

        long approvedCount = registrationRepository.countByTournamentAndStatus(tournament, RegistrationStatus.APPROVED);
        if(approvedCount >= tournament.getMaxTeams()){
            throw new RegistrationClosedException("This tournament has reached its maximum number of teams");
        }

        TeamRegistration registration = TeamRegistration.builder()
                .tournament(tournament)
                .team(team)
                .status(RegistrationStatus.PENDING)
                .build();

        return registrationMapper.toResponse(registrationRepository.save(registration));
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public TeamRegistrationResponse approveRegistration(Long registrationId, UserDetails currentUser) {
        TeamRegistration registration = findRegistrationOrThrow(registrationId);
        assertOrganizerOrAdmin(registration.getTournament(), resolveUser(currentUser));

        if(registration.getStatus() != RegistrationStatus.PENDING){
            throw new BadRequestException("Only PENDING regstrations can be approved");
        }

        long approvedCount = registrationRepository.countByTournamentAndStatus(
                registration.getTournament(), RegistrationStatus.PENDING);

        if(approvedCount >= registration.getTournament().getMaxTeams()){
            throw new RegistrationClosedException("This tournament has reached its maximum number of teams");
        }
        registration.setStatus(RegistrationStatus.APPROVED);
        return registrationMapper.toResponse(registrationRepository.save(registration));
    }

    private void assertOrganizerOrAdmin(Tournament tournament, User user) {
        boolean isOrganizer = tournament.getOrganizer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if (!isOrganizer && !isAdmin) {
            throw new AccessDeniedException("You do not organize this tournament");
        }
    }

    private TeamRegistration findRegistrationOrThrow(Long id) {
        return registrationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + id));
    }

    @Override
    @Transactional
    public TeamRegistrationResponse rejectRegistration(Long registrationId, UserDetails currentUser) {
        TeamRegistration registration = findRegistrationOrThrow(registrationId);
        assertOrganizerOrAdmin(registration.getTournament(), resolveUser(currentUser));

        if(registration.getStatus() != RegistrationStatus.PENDING){
            throw new BadRequestException("Only PENDING registration can be rejected");
        }

        registration.setStatus(RegistrationStatus.REJECTED);
        return registrationMapper.toResponse(registrationRepository.save(registration));
    }

    @Override
    public List<TeamRegistrationResponse> getRegistrationsForTournament(Long tournamentId, UserDetails currentUser) {
        Tournament tournament= tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + tournamentId));

        User user= resolveUser(currentUser);
        boolean isOrganizer = tournament.getOrganizer().getId().equals(user.getId());
        boolean isAdmin  = user.getRole().name().equals("ADMIN");

        List<TeamRegistration> registrations = (isOrganizer || isAdmin)
                ? registrationRepository.findByTournament(tournament)
                : registrationRepository.findByTournamentAndStatus(tournament, RegistrationStatus.APPROVED);

        return registrations.stream().map(registrationMapper::toResponse).toList();
    }
}
