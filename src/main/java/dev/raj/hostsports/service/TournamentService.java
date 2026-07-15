package dev.raj.hostsports.service;

import dev.raj.hostsports.dto.tournament.TournamentRequest;
import dev.raj.hostsports.dto.tournament.TournamentResponse;
import dev.raj.hostsports.entity.SportType;
import dev.raj.hostsports.entity.TournamentStatus;
import dev.raj.hostsports.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface TournamentService {
    TournamentResponse createTournament(TournamentRequest request, UserDetails currentUser);
    TournamentResponse updateTournament(Long tournamentId, TournamentRequest request, UserDetails currentUser);
    TournamentResponse getTournamentById(Long tournamentId);
    Page<TournamentResponse> searchTournaments(String city, SportType sportType, TournamentStatus status, Pageable pageable);
    List<TournamentResponse> getMyTournaments(UserDetails currentUser);
    TournamentResponse cancelTournament(Long tournamentId, UserDetails currentUser);
}
