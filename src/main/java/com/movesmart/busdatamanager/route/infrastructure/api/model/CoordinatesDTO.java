package com.movesmart.busdatamanager.route.infrastructure.api.model;

import com.movesmart.busdatamanager.route.domain.Coordinates;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;

@Generated
public record CoordinatesDTO(
        @NotNull @Min(value = -90) @Max(value = 90) double latitude,
        @NotNull @Min(value = -180) @Max(value = 180) double longitude) {
    public static CoordinatesDTO of(Double latitude, Double longitude) {
        return new CoordinatesDTO(latitude, longitude);
    }

    public Coordinates toCoordinates() {
        return new Coordinates(latitude, longitude);
    }

    public static CoordinatesDTO fromCoordinates(Coordinates coordinates) {
        return new CoordinatesDTO(coordinates.latitude(), coordinates.longitude());
    }
}
