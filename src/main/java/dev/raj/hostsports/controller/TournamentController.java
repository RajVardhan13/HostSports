package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.tournament.TournamentRequest;
import dev.raj.hostsports.dto.tournament.TournamentResponse;
import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.TournamentStatus;
import dev.raj.hostsports.service.TournamentService;
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
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Tournaments", description = "Tournament creation and browsing")
public class TournamentController {

    private final TournamentService tournamentService;

    @PostMapping
    public ResponseEntity<TournamentResponse> createTournament(@Valid @RequestBody TournamentRequest request,
                                                               @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED).body(tournamentService.createTournament(request, currentUser));
    }

    @PutMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> updateTournament(@PathVariable Long tournamentId,
                                                               @Valid @RequestBody TournamentRequest request,
                                                               @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(tournamentService.updateTournament(tournamentId, request, currentUser));
    }

    @PatchMapping("/{tournamentId}/cancel")
    public ResponseEntity<TournamentResponse> cancelTournament(@PathVariable Long tournamentId,
                                                               @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(tournamentService.cancelTournament(tournamentId,currentUser));
    }

    @GetMapping("/{tournamentId}")
    public ResponseEntity<TournamentResponse> getTournament(@PathVariable Long tournamentId){
        return ResponseEntity.ok(tournamentService.getTournamentById(tournamentId));
    }

    @GetMapping
    public ResponseEntity<Page<TournamentResponse>> searchTournaments(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) SportType sportType,
            @RequestParam(required = false)TournamentStatus status,
            Pageable pageable){
        return ResponseEntity.ok(tournamentService.searchTournaments(city,sportType,status,pageable));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TournamentResponse>> getMyTournaments(@AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(tournamentService.getMyTournaments(currentUser));
    }
}
