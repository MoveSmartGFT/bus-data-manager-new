package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto;

import com.movesmart.busdatamanager.vehicle.domain.Coordinates;
import com.movesmart.busdatamanager.vehicle.domain.Event;
import com.movesmart.busdatamanager.vehicle.domain.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Generated;

import java.util.List;

@Generated
public record VehicleRequest(@NotBlank @Pattern(regexp = "^\\d{4}[A-Z]{3}$",
        message = "Plate number must be 4 numbers and 3 letters. Example: 2020KBR") String plateNumber,
                             @NotNull Integer capacity, @NotBlank String status, @NotBlank String type,
                             @NotNull @Valid Coordinates location, @Valid List<Event> events, @NotNull double speed,
                             @NotBlank String direction, @Valid List<VehicleHistory> vehicleHistory) {
    public Vehicle toVehicle() {
        return new Vehicle(plateNumber, capacity, status, type, location, events, speed, direction, vehicleHistory);
    }
}
