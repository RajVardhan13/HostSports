package dev.raj.hostsports.dto.match;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MatchRequest {

    @NotNull
    private Long teamAId;

    @NotNull
    private Long teamBId;

    @NotNull
    @Future(message = "Match time must be in the future")
    private LocalDateTime matchDateTime;

    private Long venueId;
}
