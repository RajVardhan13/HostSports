package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Slot;
import dev.raj.hostsports.entity.Venue;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Long> {

    List<Slot> findByVenueAndAvailableTrueAndStartTimeAfterOrderByStartTime(Venue venue, LocalDateTime after);

    boolean existsByVenueAndStartTime(Venue venue, LocalDateTime startTime);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("Select s From Slot s WHERE s.id = :id")
    Optional<Slot> findByIdForUpdate(@Param("id") Long id);
}
