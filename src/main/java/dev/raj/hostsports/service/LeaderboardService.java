package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.leaderboard.LeaderboardEntryResponse;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardEntryResponse> getLeaderboard(Long tournamentId);
}
