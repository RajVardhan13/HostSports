package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.leaderboard.LeaderboardEntryResponse;
import dev.raj.hostsports.service.LeaderboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tournaments")
@RequiredArgsConstructor
@Tag(name = "Leaderboard", description = "Computed standings for a tournament")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;


    public ResponseEntity<List<LeaderboardEntryResponse>> getLeaderboard(@PathVariable Long tournamentId){
        return ResponseEntity.ok(leaderboardService.getLeaderboard(tournamentId));
    }
}
