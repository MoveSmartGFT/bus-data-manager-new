package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto;

import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.model.CoordinatesDTO;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.model.EventDTO;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.model.VehicleHistoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;

@Generated
public record VehicleResponse(
        @NotBlank String plateNumber,
        @NotNull Integer capacity,
        @NotBlank String type,
        @Valid CoordinatesDTO location,
        @Valid List<EventDTO> events,
        @NotNull double speed,
        @NotBlank String direction,
        @Valid List<VehicleHistoryDTO> vehicleHistory,
        Vehicle.Status status) {
    public static VehicleResponse fromVehicle(Vehicle vehicle) {
        return new VehicleResponse(
                vehicle.getPlateNumber(),
                vehicle.getCapacity(),
                vehicle.getType(),
                CoordinatesDTO.fromCoordinates(vehicle.getLocation()),
                vehicle.getEvents().stream().map(EventDTO::fromEvent).collect(Collectors.toList()),
                vehicle.getSpeed(),
                vehicle.getDirection(),
                vehicle.getVehicleHistory().stream()
                        .map(VehicleHistoryDTO::fromVehicleHistory)
                        .collect(Collectors.toList()),
                vehicle.getStatus());
    }
}
