package dev.raj.hostsports.dto.venue;

import dev.raj.hostsports.entity.SportType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private SportType sportType;
    private Double pricePerHour;
    private String description;
    private String imageUrl;
    private boolean active;
    private Long ownerid;
    private String ownername;
    private LocalDateTime createdAt;
}
