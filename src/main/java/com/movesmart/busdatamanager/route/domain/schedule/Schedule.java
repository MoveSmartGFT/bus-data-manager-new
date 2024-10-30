package com.movesmart.busdatamanager.route.domain.schedule;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

import java.time.LocalDateTime;

@Generated
@Embeddable
public record Schedule (
        @NotNull TypeOfDay typeOfDay,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull Integer frequencyInMinutes

) implements ValueObject {
    public static Schedule of(TypeOfDay typeOfDay, LocalDateTime startTime, LocalDateTime endTime, Integer frequencyInMinutes) {
        return new Schedule(typeOfDay, startTime, endTime, frequencyInMinutes);
    }

    @Override
    public @NotNull TypeOfDay typeOfDay() {
        return typeOfDay;
    }
}

