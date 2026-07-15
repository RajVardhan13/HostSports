package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.team.TeamRequest;
import dev.raj.hostsports.dto.team.TeamResponse;
import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class TeamMapper {

    public Team toEntity(TeamRequest request, User captain){
        return Team.builder()
                .name(request.getName())
                .sportType(request.getSportType())
                .logoUrl(request.getLogoUrl())
                .captain(captain)
                .players(new HashSet<>())
                .build();
    }

    public TeamResponse toResponse(Team team){
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .sportType(team.getSportType())
                .logoUrl(team.getLogoUrl())
                .captainId(team.getCaptain().getId())
                .captainName(team.getCaptain().getFullName())
                .playerNames(team.getPlayers().stream().map(User::getFullName).toList())
                .createdAt(team.getCreatedAt())
                .build();
    }
}
