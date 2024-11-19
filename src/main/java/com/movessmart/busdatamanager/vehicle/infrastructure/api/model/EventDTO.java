package com.movessmart.busdatamanager.vehicle.infrastructure.api.model;

import com.movessmart.busdatamanager.vehicle.domain.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.jmolecules.ddd.types.ValueObject;

public record EventDTO(
        @NotNull LocalDateTime time,
        @NotBlank String details,
        @NotNull boolean maintenance,
        @NotBlank String notification)
        implements ValueObject {
    public Event toEvent() {
        return new Event(this.time, this.details, this.maintenance, this.notification);
    }

    public static EventDTO fromEvent(Event event) {
        return new EventDTO(event.time(), event.details(), event.maintenance(), event.notification());
    }
}
