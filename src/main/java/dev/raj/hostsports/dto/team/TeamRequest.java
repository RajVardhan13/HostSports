package dev.raj.hostsports.dto.team;

import dev.raj.hostsports.entity.SportType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotBlank
    private String name;

    @NotNull
    private SportType sportType;

    private String logoUrl;
}
