package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.match.MatchRequest;
import dev.raj.hostsports.dto.match.MatchResponse;
import dev.raj.hostsports.dto.match.ScoreUpdateRequest;
import dev.raj.hostsports.service.MatchService;
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
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "Matches", description = "Match scheduling and live score updates (see WebSocket /ws for real-time push)")
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/tournaments/{tournamentId}")
    public ResponseEntity<MatchResponse> scheduleMatch(@PathVariable Long tournamentId,
                                                       @Valid @RequestBody MatchRequest request,
                                                       @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(matchService.scheduleMatch(tournamentId, request, currentUser));
    }

    @PatchMapping("/{matchId}/score")
    public ResponseEntity<MatchResponse> updateScore(@PathVariable Long matchId,
                                                     @Valid @RequestBody ScoreUpdateRequest request,
                                                     @AuthenticationPrincipal UserDetails currentUser){
        return ResponseEntity.ok(matchService.updateScore(matchId, request, currentUser));
    }

    @GetMapping("/tournaments/{tournamentId}")
    public ResponseEntity<List<MatchResponse>> getMatchesForTournament(@PathVariable Long tournamentId){
        return ResponseEntity.ok(matchService.getMatchesForTournament(tournamentId));
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResponse> getMatch(@PathVariable Long matchId){
        return ResponseEntity.ok(matchService.getMatchById(matchId));
    }
}
