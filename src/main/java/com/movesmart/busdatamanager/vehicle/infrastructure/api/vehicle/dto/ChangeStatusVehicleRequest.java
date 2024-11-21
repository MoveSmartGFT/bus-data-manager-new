package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto;

import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import jakarta.validation.Valid;

public record ChangeStatusVehicleRequest(String plateNumber, @Valid Vehicle.Status status) {
    public Vehicle toVehicle(String plateNumber) {
        return new Vehicle(plateNumber, status);
    }
}
