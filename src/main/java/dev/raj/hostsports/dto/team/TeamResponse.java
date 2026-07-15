package dev.raj.hostsports.dto.team;

import dev.raj.hostsports.entity.SportType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeamResponse {

    private Long id;
    private String name;
    private SportType sportType;
    private String logoUrl;
    private Long captainId;
    private String captainName;
    private List<String> playerNames;
    private LocalDateTime createdAt;
}
