package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto;

import com.movesmart.busdatamanager.vehicle.domain.Coordinates;
import com.movesmart.busdatamanager.vehicle.domain.Event;
import com.movesmart.busdatamanager.vehicle.domain.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;

import java.util.List;

@Generated
public record VehicleResponse(@NotBlank String plateNumber, @NotNull Integer capacity, @NotBlank String status,
                              @NotBlank String type, @Valid Coordinates location, @Valid List<Event> events, @NotNull double speed,
                              @NotBlank String direction, @Valid List<VehicleHistory> vehicleHistory) {
    public static VehicleResponse fromVehicle(Vehicle vehicle) {
        return new VehicleResponse(vehicle.getPlateNumber(), vehicle.getCapacity(), vehicle.getStatus(),
                vehicle.getType(), vehicle.getLocation(), vehicle.getEvents(), vehicle.getSpeed(), vehicle.getDirection(),
                vehicle.getVehicleHistory());
    }
}
