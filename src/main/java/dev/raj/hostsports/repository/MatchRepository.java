package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.entity.MatchStatus;
import dev.raj.hostsports.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByTournamentOrderByMatchDateTime(Tournament tournament);

    List<Match> findByTournamentAndStatus(Tournament tournament, MatchStatus status);

    List<Match> findByTournamentAndStatusIn(Tournament tournament, List<MatchStatus> statuses);
}
