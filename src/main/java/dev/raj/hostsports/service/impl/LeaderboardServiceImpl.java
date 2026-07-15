package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.leaderboard.LeaderboardEntryResponse;
import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.entity.MatchStatus;
import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.Tournament;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.repository.MatchRepository;
import dev.raj.hostsports.repository.TournamentRepository;
import dev.raj.hostsports.service.LeaderboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;

    @Override
    public List<LeaderboardEntryResponse> getLeaderboard(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + tournamentId));

        List<Match> completedMatches = matchRepository.findByTournamentAndStatus(tournament, MatchStatus.COMPLETED);

        Map<Long, LeaderboardEntryResponse> table = new LinkedHashMap<>();

        for (Match match : completedMatches){
            LeaderboardEntryResponse a = table.computeIfAbsent(match.getTeamA().getId(), id -> blankEntry(match.getTeamA()));
            LeaderboardEntryResponse b = table.computeIfAbsent(match.getTeamB().getId(), id -> blankEntry(match.getTeamB()));

            a.setPlayed(a.getPlayed() + 1);
            b.setPlayed(b.getPlayed() + 1);
            a.setGoalsFor(a.getGoalsFor() + match.getScoreTeamA());
            a.setGoalsAgainst(a.getGoalsAgainst() + match.getScoreTeamB());
            b.setGoalsFor(b.getGoalsFor() + match.getScoreTeamB());
            b.setGoalsAgainst(b.getGoalsAgainst() + match.getScoreTeamA());

            switch (match.getResult()){
                case TEAM_A_WIN -> {
                    a.setWon(a.getWon() + 1);
                    a.setPoints(a.getPoints() + 3);
                    b.setLost(b.getLost() + 1);
                }
                case TEAM_B_WIN -> {
                    b.setWon(b.getWon() + 1);
                    b.setPoints(b.getPoints() + 3);
                    b.setLost(a.getLost() + 1);
                }
                case DRAW -> {
                    a.setDraw(a.getDraw() + 1);
                    b.setDraw(b.getDraw() + 1);
                    a.setPoints(a.getPoints() + 1);
                    b.setPoints(b.getPoints() + 1);
                }
            }
        }

        return table.values().stream()
                .sorted(
                        Comparator.comparingInt(LeaderboardEntryResponse::getPoints).reversed()
                                .thenComparing(Comparator.comparingInt(
                                        (LeaderboardEntryResponse e) -> e.getGoalsFor() - e.getGoalsAgainst()).reversed())
                ).toList();
    }

    private LeaderboardEntryResponse blankEntry(Team team) {
        return LeaderboardEntryResponse.builder()
                .teamId(team.getId())
                .teamName(team.getName())
                .played(0)
                .won(0)
                .lost(0)
                .draw(0)
                .goalsFor(0)
                .goalsAgainst(0)
                .points(0)
                .build();
    }
}
