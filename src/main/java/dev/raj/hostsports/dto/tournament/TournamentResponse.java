package dev.raj.hostsports.dto.tournament;

import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.TournamentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TournamentResponse {
    private Long id;
    private String name;
    private SportType sportType;
    private String city;
    private LocalDateTime registrationDeadline;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer maxTeams;
    private String description;
    private TournamentStatus status;
    private Long organizerId;
    private String organizerName;
    private Long venueId;
    private String venueName;
    private LocalDateTime createdAt;
}
