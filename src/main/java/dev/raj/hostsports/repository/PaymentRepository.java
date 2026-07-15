package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.Match;
import dev.raj.hostsports.entity.MatchStatus;
import dev.raj.hostsports.entity.Payment;
import dev.raj.hostsports.entity.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

    Optional<Payment> findByTransactionRef(String transactionRef);
}
