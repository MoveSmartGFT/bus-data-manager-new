package com.moveSmart.busDataManager.route.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

@Generated
public record Coordinates (
        @NotNull  double latitude,
        @NotNull  double longitude
) implements ValueObject {
    public static Coordinates of(Double latitude, Double longitude) {
        return new Coordinates(latitude, longitude);
    }
}