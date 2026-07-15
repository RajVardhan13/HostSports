package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.tournament.TournamentRequest;
import dev.raj.hostsports.dto.tournament.TournamentResponse;
import dev.raj.hostsports.entity.Tournament;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public Tournament toEntity(TournamentRequest request, User organizer, Venue venue){
        return Tournament.builder()
                .name(request.getName())
                .sportType(request.getSportType())
                .city(request.getCity())
                .registrationDeadLine(request.getRegistrationDeadLine())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .maxTeams(request.getMaxTeams())
                .description(request.getDescription())
                .organizer(organizer)
                .venue(venue)
                .build();
    }

    public TournamentResponse toResponse(Tournament tournament){
        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .sportType(tournament.getSportType())
                .city(tournament.getCity())
                .registrationDeadline(tournament.getRegistrationDeadLine())
                .startDate(tournament.getStartDate())
                .endDate(tournament.getEndDate())
                .maxTeams(tournament.getMaxTeams())
                .description(tournament.getDescription())
                .status(tournament.getStatus())
                .organizerId(tournament.getOrganizer().getId())
                .organizerName(tournament.getOrganizer().getFullName())
                .venueId(tournament.getVenue() != null ? tournament.getVenue().getId() : null)
                .venueName(tournament.getVenue() != null ? tournament.getVenue().getName() : null)
                .createdAt(tournament.getCreatedAt())
                .build();
    }
}
