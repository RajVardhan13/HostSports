package dev.raj.hostsports.controller;

import dev.raj.hostsports.dto.MatchSummaryRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiTestController {

    private final ChatClient chatClient;

    public AiTestController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/ping")
    public String ping(@RequestParam String question){
        return chatClient.prompt()
                .system("""
                        You are a helpful assistant for HostMySports,
                        a sports venue booking and tournament management app.
                        Keep answers short, friendly, and sports-related.
                        If asked something unrelated to sports or booking, politely redirect.
                        """)
                .user(question)
                .call()
                .content();
    }

    @PostMapping("/match-summary")
    public String matchSummary(@RequestBody MatchSummaryRequest request) {

        String prompt = String.format("""
            Write a short, exciting one-paragraph match summary for a sports app.
            Team A: %s scored %d
            Team B: %s scored %d
            Mention who won and make it sound engaging, like a sports commentator.
            """,
                request.teamA(), request.scoreA(),
                request.teamB(), request.scoreB()
        );

        return chatClient.prompt()
                .system("You are a sports commentator writing match recaps for HostMySports.")
                .user(prompt)
                .call()
                .content();
    }
}
