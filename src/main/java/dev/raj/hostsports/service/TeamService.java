package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.team.AddPlayerRequest;
import dev.raj.hostsports.dto.team.TeamRequest;
import dev.raj.hostsports.dto.team.TeamResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TeamService {
    TeamResponse createTeam(TeamRequest request, UserDetails currentUser);
    TeamResponse addPlayer(Long teamId, AddPlayerRequest request, UserDetails currentUser);
    TeamResponse getTeamById(Long teamId);
    List<TeamResponse> getAllTeams();
    List<TeamResponse> getMyTeams(UserDetails currentUser);
}
