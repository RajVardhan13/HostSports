package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findByOwner(User owner);

    Page<Venue> findByActiveTrue(Pageable pageable);

    @Query("""
            SELECT v FROM Venue v
            WHERE v.active = true
             AND (:city IS NULL OR LOWER(v.city) = LOWER(:city))
             AND (:sportType IS NULL OR v.sportType = :sportType)
           """)
    Page<Venue> search(@Param("city") String city,
                       @Param("sportType")SportType sportType,
                       Pageable pageable);

}
