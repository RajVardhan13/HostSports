package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.RegistrationStatus;
import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.TeamRegistration;
import dev.raj.hostsports.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRegistrationRepository extends JpaRepository<TeamRegistration, Long> {

    List<TeamRegistration> findByTournament(Tournament tournament);

    List<TeamRegistration> findByTournamentAndStatus(Tournament tournament, RegistrationStatus status);

    Optional<TeamRegistration> findByTournamentAndTeam(Tournament tournament, Team team);

    long countByTournamentAndStatus(Tournament tournament, RegistrationStatus status);
}
