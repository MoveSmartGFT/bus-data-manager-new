package com.movessmart.busdatamanager.vehicle.infrastructure.api.model;

import com.movessmart.busdatamanager.vehicle.domain.VehicleHistory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import org.jmolecules.ddd.types.ValueObject;

public record VehicleHistoryDTO(
        String id,
        @NotBlank String routeId,
        @NotBlank String driverId,
        @NotNull LocalDateTime startTime,
        LocalDateTime endTime)
        implements ValueObject {
    public VehicleHistory toVehicleHistory() {
        return new VehicleHistory(routeId, driverId, startTime);
    }

    public static VehicleHistoryDTO fromVehicleHistory(VehicleHistory vehicleHistory) {
        return new VehicleHistoryDTO(
                vehicleHistory.getId(),
                vehicleHistory.getRouteId(),
                vehicleHistory.getDriverId(),
                vehicleHistory.getStartTime(),
                vehicleHistory.getEndTime());
    }
}
