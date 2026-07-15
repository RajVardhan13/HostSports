package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Booking;
import dev.raj.hostsports.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByPlayerOrderByBookedAtDesc(User player);

    Optional<Booking> findBySlotId(Long slotId);
}
