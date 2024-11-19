package com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto;

import com.movessmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.model.CoordinatesDTO;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.model.EventDTO;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.model.VehicleHistoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

public record UpdateVehicleRequest(
        @NotBlank
                @Pattern(
                        regexp = "^\\d{4}[A-Z]{3}$",
                        message = "Plate number must be 4 numbers and 3 letters. Example: 2020KBR")
                String plateNumber,
        @NotNull Integer capacity,
        @NotBlank String type,
        @NotNull @Valid CoordinatesDTO location,
        @Valid List<EventDTO> events,
        @NotNull double speed,
        @NotBlank String direction,
        @Valid List<VehicleHistoryDTO> vehicleHistory) {
    public Vehicle toVehicle(String plateNumber) {
        return new Vehicle(
                plateNumber,
                capacity,
                type,
                location.toCoordinates(),
                events.stream().map(EventDTO::toEvent).collect(Collectors.toList()),
                speed,
                direction,
                vehicleHistory.stream().map(VehicleHistoryDTO::toVehicleHistory).collect(Collectors.toList()));
    }
}
