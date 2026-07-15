package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.match.MatchRequest;
import dev.raj.hostsports.dto.match.MatchResponse;
import dev.raj.hostsports.dto.match.ScoreUpdateRequest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface MatchService {
    MatchResponse scheduleMatch(Long tournametId, MatchRequest request, UserDetails currentUser);
    MatchResponse updateScore(Long matchId, ScoreUpdateRequest request, UserDetails currentUser);
    List<MatchResponse> getMatchesForTournament(Long tournamentId);
    MatchResponse getMatchById(Long matchId);
}
