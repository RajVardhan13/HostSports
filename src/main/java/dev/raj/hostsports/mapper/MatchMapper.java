package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.match.MatchRequest;
import dev.raj.hostsports.dto.match.MatchResponse;
import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.Tournament;
import dev.raj.hostsports.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class MatchMapper {

    public Match toEntity(MatchRequest request, Tournament tournament, Team teamA, Team teamB, Venue venue){
        return Match.builder()
                .tournament(tournament)
                .teamA(teamA)
                .teamB(teamB)
                .matchDateTime(request.getMatchDateTime())
                .venue(venue)
                .build();
    }

    public MatchResponse toResponse(Match match){
        return MatchResponse.builder()
                .id(match.getId())
                .tournamentId(match.getTournament().getId())
                .teamAId(match.getTeamA().getId())
                .teamAName(match.getTeamA().getName())
                .teamBId(match.getTeamB().getId())
                .teamBName(match.getTeamB().getName())
                .matchDateTime(match.getMatchDateTime())
                .venueId(match.getVenue() != null ? match.getVenue().getId() : null)
                .venueName(match.getVenue() != null ? match.getVenue().getName() : null)
                .status(match.getStatus())
                .scoreTeamA(match.getScoreTeamA())
                .scoreTeamB(match.getScoreTeamB())
                .result(match.getResult())
                .updatedAt(match.getUpdatedAt())
                .build();
    }
}
