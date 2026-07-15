package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.registration.TeamRegistrationResponse;
import dev.raj.hostsports.entity.TeamRegistration;
import org.springframework.stereotype.Component;

@Component
public class TeamRegistrationMapper {

    public TeamRegistrationResponse toResponse(TeamRegistration registration){
        return TeamRegistrationResponse.builder()
                .id(registration.getId())
                .tournamentId(registration.getTournament().getId())
                .tournamentName(registration.getTournament().getName())
                .teamId(registration.getTeam().getId())
                .teamName(registration.getTeam().getName())
                .status(registration.getStatus())
                .registeredAt(registration.getRegisterAt())
                .build();
    }
}
