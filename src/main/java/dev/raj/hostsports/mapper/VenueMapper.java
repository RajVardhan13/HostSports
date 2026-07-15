package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.venue.VenueRequest;
import dev.raj.hostsports.dto.venue.VenueResponse;
import dev.raj.hostsports.entity.User;
import dev.raj.hostsports.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenueMapper {

    public Venue toEntity(VenueRequest request, User owner){
        return Venue.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .sportType(request.getSportType())
                .pricePerHour(request.getPricePerHour())
                .description(request.getDescription())
                .imageUrl(request.getImageUrl())
                .owner(owner)
                .active(true)
                .build();
    }

    public void updateEntity(Venue venue, VenueRequest request){
        venue.setName(request.getName());
        venue.setAddress(request.getAddress());
        venue.setCity(request.getCity());
        venue.setSportType(request.getSportType());
        venue.setPricePerHour(request.getPricePerHour());
        venue.setDescription(request.getDescription());
        venue.setImageUrl(request.getImageUrl());
    }

    public VenueResponse toResponse(Venue venue){
        return VenueResponse.builder()
                .id(venue.getId())
                .name(venue.getName())
                .address(venue.getAddress())
                .city(venue.getCity())
                .sportType(venue.getSportType())
                .pricePerHour(venue.getPricePerHour())
                .description(venue.getDescription())
                .imageUrl(venue.getImageUrl())
                .active(venue.isActive())
                .ownerid(venue.getOwner().getId())
                .ownername(venue.getOwner().getFullName())
                .createdAt(venue.getCreatedAt())
                .build();
    }
}
