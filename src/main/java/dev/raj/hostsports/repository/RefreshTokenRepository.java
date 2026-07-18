package dev.raj.hostsports.repository;

import dev.raj.hostsports.entity.RefreshToken;
import dev.raj.hostsports.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshToken r where r.user = :user")
    void deleteByUser(User user);
}
