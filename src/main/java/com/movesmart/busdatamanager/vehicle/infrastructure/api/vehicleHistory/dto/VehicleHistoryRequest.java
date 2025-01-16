package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto;

import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Generated;

@Generated
public record VehicleHistoryRequest(
        @NotBlank String routeId, @NotNull String driverId, @NotNull String vehicleId, LocalDateTime startTime) {
    public VehicleHistory toVehicleHistory() {
        return new VehicleHistory(routeId, driverId, vehicleId, startTime);
    }
}
