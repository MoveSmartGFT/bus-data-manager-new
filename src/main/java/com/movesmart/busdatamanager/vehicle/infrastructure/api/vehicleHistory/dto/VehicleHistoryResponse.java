package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto;

import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Generated;

@Generated
public record VehicleHistoryResponse(
        @NotBlank String id,
        @NotBlank String vehicleId,
        @NotBlank String routeId,
        @NotBlank String driverId,
        @NotBlank LocalDateTime startTime,
        @NotBlank LocalDateTime endTime) {
    public static VehicleHistoryResponse fromVehicleHistory(VehicleHistory vehicleHistory) {
        return new VehicleHistoryResponse(
                vehicleHistory.getId(),
                vehicleHistory.getVehicleId(),
                vehicleHistory.getRouteId(),
                vehicleHistory.getDriverId(),
                vehicleHistory.getStartTime(),
                vehicleHistory.getEndTime());
    }
}
