package dev.raj.hostsports.dto.match;

import dev.raj.hostsports.entity.MatchStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ScoreUpdateRequest {

    @NotNull
    @Min(0)
    private Integer scoreTeamA;

    @NotNull
    @Min(0)
    private Integer scoreTeamB;

    @NotNull(message = "Status is required, e.g. LIVE or COMPLETED")
    private MatchStatus status;
}
