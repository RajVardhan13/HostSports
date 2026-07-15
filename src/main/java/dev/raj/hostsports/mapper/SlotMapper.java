package dev.raj.hostsports.mapper;

import dev.raj.hostsports.dto.slot.SlotRequest;
import dev.raj.hostsports.dto.slot.SlotResponse;
import dev.raj.hostsports.entity.Slot;
import dev.raj.hostsports.entity.Venue;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {

    public Slot toEntity(SlotRequest request, Venue venue){
        return Slot.builder()
                .venue(venue)
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .available(true)
                .build();
    }

    public SlotResponse toResponse(Slot slot){
        return SlotResponse.builder()
                .id(slot.getId())
                .venueId(slot.getVenue().getId())
                .venueName(slot.getVenue().getName())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .available(slot.isAvailable())
                .build();
    }
}
