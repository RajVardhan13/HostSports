package dev.raj.hostsports.dto.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LeaderboardEntryResponse {
    private Long teamId;
    private String teamName;
    private int played;
    private int won;
    private int lost;
    private int draw;
    private int goalsFor;
    private int goalsAgainst;
    private int points;
}
