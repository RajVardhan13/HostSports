package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.dto.match.MatchRequest;
import dev.raj.hostsports.dto.match.MatchResponse;
import dev.raj.hostsports.dto.match.ScoreUpdateRequest;
import dev.raj.hostsports.entity.*;
import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.exception.BadRequestException;
import dev.raj.hostsports.mapper.MatchMapper;
import dev.raj.hostsports.repository.*;
import dev.raj.hostsports.service.LeaderboardService;
import dev.raj.hostsports.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final MatchMapper matchMapper;
    private final LeaderboardService leaderboardService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public MatchResponse scheduleMatch(Long tournametId, MatchRequest request, UserDetails currentUser) {
        Tournament tournament = tournamentRepository.findById(tournametId)
                .orElseThrow(()-> new ResourceNotFoundException("Tournament not found with id: " + tournametId));

        assertOrganizerOrAdmin(tournament, resolveUser(currentUser));

        if(request.getTeamAId().equals(request.getTeamBId())){
            throw new BadRequestException("A team cannot play against itself");
        }

        Team teamA = teamRepository.findById(request.getTeamAId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.getTeamAId()));
        Team teamB = teamRepository.findById(request.getTeamBId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with id: " + request.getTeamBId()));

        Venue venue = null;
        if(request.getVenueId() != null){
            venue = venueRepository.findById(request.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id: " + request.getVenueId()));
        }

        Match match = matchMapper.toEntity(request, tournament, teamA, teamB, venue);
        return matchMapper.toResponse(matchRepository.save(match));

    }

    private User resolveUser(UserDetails currentUser) {
        return userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public MatchResponse updateScore(Long matchId, ScoreUpdateRequest request, UserDetails currentUser) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId));

        assertOrganizerOrAdmin(match.getTournament(), resolveUser(currentUser));

        if(match.getStatus() == MatchStatus.COMPLETED){
            throw new BadRequestException("Cannot update the score of a completed match");
        }

        match.setScoreTeamA(request.getScoreTeamA());
        match.setScoreTeamB(request.getScoreTeamB());
        match.setStatus(request.getStatus());

        if(request.getStatus() == MatchStatus.COMPLETED){
            if(request.getScoreTeamA() > request.getScoreTeamB()){
                match.setResult(MatchResult.TEAM_A_WIN);
            } else if(request.getScoreTeamB() > request.getScoreTeamA()){
                match.setResult(MatchResult.TEAM_B_WIN);
            } else{
                match.setResult(MatchResult.DRAW);
            }
        }

        MatchResponse response = matchMapper.toResponse(matchRepository.save(match));

        messagingTemplate.convertAndSend("/topic/matches" + matchId, response);

        if(request.getStatus() == MatchStatus.COMPLETED){
            Long tournamentId = match.getTournament().getId();
            messagingTemplate.convertAndSend(
                    "/topic/tournaments/" + tournamentId + "/leaderboard",
                    leaderboardService.getLeaderboard(tournamentId));
        }

        return response;
    }

    private void assertOrganizerOrAdmin(Tournament tournament, User user) {
        boolean isOrganizer = tournament.getOrganizer().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");
        if (!isOrganizer && !isAdmin) {
            throw new AccessDeniedException("You do not organize this tournament");
        }
    }

    @Override
    public List<MatchResponse> getMatchesForTournament(Long tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found with id: " + tournamentId));

        return matchRepository.findByTournamentOrderByMatchDateTime(tournament).stream()
                .map(matchMapper::toResponse)
                .toList();
    }

    @Override
    public MatchResponse getMatchById(Long matchId) {
        return matchMapper.toResponse(matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found with id: " + matchId)));
    }
}
