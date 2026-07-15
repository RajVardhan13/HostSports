package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.team.AddPlayerRequest;
import dev.raj.hostsports.dto.team.TeamRequest;
import dev.raj.hostsports.dto.team.TeamResponse;
import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.exception.DuplicateResourceException;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.TeamMapper;
import dev.raj.hostsports.repository.TeamRepository;
import dev.raj.hostsports.repository.UserRepository;
import dev.raj.hostsports.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TeamResponse createTeam(TeamRequest request, UserDetails currentUser) {
        User captain = resolveUser(currentUser);

        if(teamRepository.existsByNameAndCaptain(request.getName(), captain)){
            throw new DuplicateResourceException("You already have a team with this name");
        }

        Team team = teamMapper.toEntity(request, captain);
        return teamMapper.toResponse(teamRepository.save(team));
    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public TeamResponse addPlayer(Long teamId, AddPlayerRequest request, UserDetails currentUser) {
        Team team = findTeamOrThrow(teamId);
        User captain = resolveUser(currentUser);

        boolean isCaptain = team.getCaptain().getId().equals(captain.getId());
        boolean isAdmin = captain.getRole().name().equals("ADMIN");
        if(!isCaptain && !isAdmin){
            throw new AccessDeniedException("Only the team captain can add players");
        }

        User player = userRepository.findById(request.getPlayerId())
                .orElseThrow(() -> new ResourceNotFoundException("Player not found with id: " + request.getPlayerId()));

        if(team.getPlayers().contains(player)){
            throw new BadRequestException("This player is already on the team");
        }

        team.getPlayers().add(player);
        return teamMapper.toResponse(teamRepository.save(team));
    }

    private Team findTeamOrThrow(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + id));
    }

    @Override
    public TeamResponse getTeamById(Long teamId) {
        return teamMapper.toResponse(findTeamOrThrow(teamId));
    }

    @Override
    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream().map(teamMapper::toResponse).toList();
    }

    @Override
    public List<TeamResponse> getMyTeams(UserDetails currentUser) {
        User captain = resolveUser(currentUser);
        return teamRepository.findByCaptain(captain).stream().map(teamMapper::toResponse).toList();
    }
}
