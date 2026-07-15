package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.Tournament;
import dev.raj.hostsports.entity.TournamentStatus;
import dev.raj.hostsports.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByOrganizer(User organizer);

    @Query(""" 
           SELECT t FROM Tournament t
           WHERE (:city IS NULL OR LOWER(t.city) = LOWER(:city))
            AND (:sportType IS NULL OR t.sportType = :sportType)
            AND (:status IS NULL OR t.status = :status)
           """)
    Page<Tournament> search(@Param("city") String city,
                            @Param("sportType")SportType sportType,
                            @Param("status")TournamentStatus status,
                            Pageable pageable);
}
