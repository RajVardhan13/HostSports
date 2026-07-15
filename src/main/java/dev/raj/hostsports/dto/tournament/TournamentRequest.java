package dev.raj.hostsports.dto.tournament;

import dev.raj.hostsports.entity.SportType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentRequest {
    @NotBlank
    private String name;

    @NotNull
    private SportType sportType;

    private String city;

    @NotNull
    @Future(message = "Registration deadline must be in the future")
    private LocalDateTime registrationDeadLine;

    @NotNull
    @Future(message = "Start date must be in the future")
    private LocalDateTime startDate;

    @NotNull
    @Future(message = "End date must be in the future")
    private LocalDateTime endDate;

    @NotNull
    @Positive
    private Integer maxTeams;

    private String description;

    private Long venueId;
}
