package dev.raj.hostsports.dto;

public record MatchSummaryRequest(
        String teamA,
        String teamB,
        int scoreA,
        int scoreB
) {}
