package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Team;
import dev.raj.hostsports.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByCaptain(User captain);

    boolean existsByNameAndCaptain(String name, User captain);
}
