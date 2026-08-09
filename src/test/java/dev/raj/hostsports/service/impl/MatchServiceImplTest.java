package dev.raj.hostsports.service.impl;

import dev.raj.hostsports.exception.ResourceNotFoundException;
import dev.raj.hostsports.mapper.MatchMapper;
import dev.raj.hostsports.repository.*;
import dev.raj.hostsports.service.AiMatchSummaryService;
import dev.raj.hostsports.service.LeaderboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.dto.match.MatchResponse;
import java.util.Optional;
import static org.mockito.Mockito.*;
import dev.raj.hostsports.entity.*;
import dev.raj.hostsports.dto.match.ScoreUpdateRequest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private VenueRepository venueRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MatchMapper matchMapper;

    @Mock
    private LeaderboardService leaderboardService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private AiMatchSummaryService aiMatchSummaryService;

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void sanityCheck() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void whenMatchExists_thenReturnMatchResponse() {

        Long matchId = 1L;
        Match fakeMatch = new Match();
        fakeMatch.setId(matchId);

        MatchResponse fakeResponse = new MatchResponse();
        fakeResponse.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(fakeMatch));
        when(matchMapper.toResponse(fakeMatch)).thenReturn(fakeResponse);

        MatchResponse result = matchService.getMatchById(matchId);

        assertNotNull(result);
        assertEquals(matchId, result.getId());
        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void whenMatchDoesNotExist_thenThrowResourceNotFoundException() {
        Long matchId = 999L;
        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            matchService.getMatchById(matchId);
        });
    }

    @Test
    void whenMatchCompleted_thenAiSummaryServiceIsTriggered() {

        Long matchId = 1L;

        Match match = new Match();
        match.setId(matchId);
        match.setStatus(MatchStatus.SCHEDULED);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        User organizer = new User();
        organizer.setId(1L);
        tournament.setOrganizer(organizer);
        match.setTournament(tournament);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.ORGANIZER);
        currentUser.setEmail("organizer@test.com");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("organizer@test.com")
                .password("password")
                .authorities("ROLE_ORGANIZER")
                .build();

        ScoreUpdateRequest request = new ScoreUpdateRequest();
        request.setScoreTeamA(3);
        request.setScoreTeamB(1);
        request.setStatus(MatchStatus.COMPLETED);

        MatchResponse fakeResponse = new MatchResponse();
        fakeResponse.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(userRepository.findByEmail("organizer@test.com")).thenReturn(Optional.of(currentUser));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toResponse(match)).thenReturn(fakeResponse);
        when(leaderboardService.getLeaderboard(anyLong())).thenReturn(java.util.Collections.emptyList());

        matchService.updateScore(matchId, request, userDetails);

        verify(aiMatchSummaryService, times(1)).generateAndBroadcastSummary(match);
    }

    @Test
    void whenMatchNotCompleted_thenAiSummaryServiceIsNotTriggered() {

        Long matchId = 1L;

        Match match = new Match();
        match.setId(matchId);
        match.setStatus(MatchStatus.SCHEDULED);

        Tournament tournament = new Tournament();
        tournament.setId(1L);
        User organizer = new User();
        organizer.setId(1L);
        tournament.setOrganizer(organizer);
        match.setTournament(tournament);

        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setRole(Role.ORGANIZER);
        currentUser.setEmail("organizer@test.com");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("organizer@test.com")
                .password("password")
                .authorities("ROLE_ORGANIZER")
                .build();

        ScoreUpdateRequest request = new ScoreUpdateRequest();
        request.setScoreTeamA(1);
        request.setScoreTeamB(0);
        request.setStatus(MatchStatus.SCHEDULED);

        MatchResponse fakeResponse = new MatchResponse();
        fakeResponse.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(match));
        when(userRepository.findByEmail("organizer@test.com")).thenReturn(Optional.of(currentUser));
        when(matchRepository.save(any(Match.class))).thenReturn(match);
        when(matchMapper.toResponse(match)).thenReturn(fakeResponse);

        matchService.updateScore(matchId, request, userDetails);

        verify(aiMatchSummaryService, never()).generateAndBroadcastSummary(any());
    }
}