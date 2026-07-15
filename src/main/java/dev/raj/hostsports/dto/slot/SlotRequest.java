package dev.raj.hostsports.dto.slot;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SlotRequest {

    @NotNull
    @Future(message = "Slot start time must be in the future")
    private LocalDateTime startTime;

    @NotNull
    @Future(message = "Slot end time must be in the future")
    private LocalDateTime endTime;
}
