package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.registration.TeamRegistrationRequest;
import dev.raj.hostsports.dto.registration.TeamRegistrationResponse;
import dev.raj.hostsports.service.TeamRegistrationService;
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
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
@Tag(name = "Registartions", description = "Team registrations for tournaments")
public class TeamRegistrationController {

    private final TeamRegistrationService registrationService;

    @PostMapping("/tournaments/{tournamentId}")
    public ResponseEntity<TeamRegistrationResponse> registerTeam(@PathVariable Long tournamentId,
                                                                 @Valid @RequestBody TeamRegistrationRequest request,
                                                                 @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.registerTeam(tournamentId, request, currentUser));
    }

    @PatchMapping("/{registrationId}/approv")
    public ResponseEntity<TeamRegistrationResponse> approve(@PathVariable Long registrationId,
                                                            @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(registrationService.approveRegistration(registrationId, currentUser));
    }

    @PatchMapping("/{registrationId}/reject")
    public ResponseEntity<TeamRegistrationResponse> reject(@PathVariable Long registartionId,
                                                           @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(registrationService.rejectRegistration(registartionId, currentUser));
    }

    @GetMapping("/tournaments/{tournamentId}")
    public ResponseEntity<List<TeamRegistrationResponse>> getForTournament(@PathVariable Long tournamentId,
                                                                           @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(registrationService.getRegistrationsForTournament(tournamentId, currentUser));
    }
}
