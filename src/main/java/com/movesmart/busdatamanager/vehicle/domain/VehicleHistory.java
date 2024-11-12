package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import org.jmolecules.ddd.types.ValueObject;

import java.time.LocalDateTime;

@Generated
public record VehicleHistory(
        @NotBlank String id,
        @NotBlank String routeId,
        @Valid Driver driver,
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
        ) implements ValueObject {
    public static VehicleHistory of(String id, String routeId, Driver driver, LocalDateTime startTime, LocalDateTime endTime) {
        return new VehicleHistory(id, routeId, driver, startTime, endTime);
    }
}