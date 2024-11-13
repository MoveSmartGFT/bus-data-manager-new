package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

import java.time.LocalDateTime;

@Generated
public record Event(
        @NotNull LocalDateTime time, @NotBlank String details,
        @NotNull boolean maintenance, @NotBlank String notification
) implements ValueObject {
    public static Event of(LocalDateTime time, String details, boolean maintenance, String notification) {
        return new Event(time, details, maintenance, notification);
    }
}