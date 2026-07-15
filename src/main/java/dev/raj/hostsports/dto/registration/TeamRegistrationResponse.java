package dev.raj.hostsports.dto.registration;

import dev.raj.hostsports.entity.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRegistrationResponse {
    private Long id;
    private Long tournamentId;
    private String tournamentName;
    private Long teamId;
    private String teamName;
    private RegistrationStatus status;
    private LocalDateTime registeredAt;
}
