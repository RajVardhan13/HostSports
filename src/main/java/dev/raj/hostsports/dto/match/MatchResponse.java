package dev.raj.hostsports.dto.match;

import dev.raj.hostsports.entity.MatchResult;
import dev.raj.hostsports.entity.MatchStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MatchResponse {
    private Long id;
    private Long tournamentId;
    private Long teamAId;
    private String teamAName;
    private Long teamBId;
    private String teamBName;
    private LocalDateTime matchDateTime;
    private Long venueId;
    private String venueName;
    private MatchStatus status;
    private Integer scoreTeamA;
    private Integer scoreTeamB;
    private MatchResult result;
    private LocalDateTime updatedAt;
}
