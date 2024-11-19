package com.movessmart.busdatamanager.route.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

@Generated
@Embeddable
public record Schedule(
        @NotNull @Enumerated TypeOfDay typeOfDay,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime,
        @NotNull Integer frequencyInMinutes)
        implements ValueObject {
    public static Schedule of(
            TypeOfDay typeOfDay, LocalDateTime startTime, LocalDateTime endTime, Integer frequencyInMinutes) {
        return new Schedule(typeOfDay, startTime, endTime, frequencyInMinutes);
    }

    public enum TypeOfDay {
        WEEKDAY,
        WEEKEND
    }
}
