package dev.raj.hostsports.service;

import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.entity.MatchResult;
import dev.raj.hostsports.entity.Team;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiMatchSummaryServiceTest {

    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private AiMatchSummaryService aiMatchSummaryService;

    @Test
    void sanityCheck() {
    }

    @Test
    void whenCalled_thenBroadcastsAiGeneratedSummary() {

        Team teamA = new Team();
        teamA.setName("Thunder FC");
        Team teamB = new Team();
        teamB.setName("Lightning United");

        Match match = new Match();
        match.setId(1L);
        match.setTeamA(teamA);
        match.setTeamB(teamB);
        match.setScoreTeamA(3);
        match.setScoreTeamB(1);
        match.setResult(MatchResult.TEAM_A_WIN);

        ChatClient chatClient = mock(ChatClient.class);
        ChatClient.ChatClientRequestSpec requestSpec = mock(ChatClient.ChatClientRequestSpec.class);
        ChatClient.CallResponseSpec responseSpec = mock(ChatClient.CallResponseSpec.class);

        when(chatClientBuilder.build()).thenReturn(chatClient);
        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content()).thenReturn("Thunder FC won a thrilling 3-1 victory!");


        aiMatchSummaryService.generateAndBroadcastSummary(match);

        verify(messagingTemplate, times(1)).convertAndSend(
                eq("/topic/matches/1/summary"),
                eq("Thunder FC won a thrilling 3-1 victory!")
        );
    }
}