package com.movessmart.busdatamanager.vehicle.infrastructure.api.model;

import com.movessmart.busdatamanager.vehicle.domain.Coordinates;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

@Generated
public record CoordinatesDTO(
        @NotNull @Min(value = -90) @Max(value = 90) double latitude,
        @NotNull @Min(value = -180) @Max(value = 180) double longitude)
        implements ValueObject {
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
