package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.registration.TeamRegistrationRequest;
import dev.raj.hostsports.dto.registration.TeamRegistrationResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TeamRegistrationService {
    TeamRegistrationResponse registerTeam(Long tournamentId, TeamRegistrationRequest request, UserDetails curremtUser);
    TeamRegistrationResponse approveRegistration(Long registrationId, UserDetails currentUser);
    TeamRegistrationResponse rejectRegistration(Long registrationId, UserDetails currentUser);
    List<TeamRegistrationResponse> getRegistrationsForTournament(Long tournamentId, UserDetails currentUser);
}
