package dev.raj.hostsports.service;

import dev.raj.hostsports.entity.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiMatchSummaryService {

    private final ChatClient.Builder chatClientBuilder;
    private final SimpMessagingTemplate messagingTemplate;

    @Async
    public void generateAndBroadcastSummary(Match match) {
        ChatClient chatClient = chatClientBuilder.build();

        String prompt = String.format("""
                        Write a short, exciting one-paragraph match summary for a sports app.
                        Team A: %s scored %d
                        Team B: %s scored %d
                        Result: %s
                        Mention who won and make it sound engaging, like a sports commentator.
                        """,
                match.getTeamA().getName(), match.getScoreTeamA(),
                match.getTeamB().getName(), match.getScoreTeamB(),
                match.getResult()
        );

        String summary = chatClient.prompt()
                .system("You are a sports commentator writing match recaps for HostMySports.")
                .user(prompt)
                .call()
                .content();

        messagingTemplate.convertAndSend(
                "/topic/matches/" + match.getId() + "/summary",
                summary);
    }
}