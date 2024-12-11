package com.movesmart.busdatamanager.vehicle.domain.vehicleHistory;

import io.hypersistence.tsid.TSID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Identity;

@Getter
@RequiredArgsConstructor
@Generated
public class VehicleHistory {
    @Identity
    @NotBlank
    private String id;

    @NotBlank
    private String routeId;

    @NotBlank
    private String driverId;

    @NotNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public VehicleHistory(String routeId, String driverId, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = TSID.Factory.getTsid().toString();
        this.routeId = routeId;
        this.driverId = driverId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public VehicleHistory(String routeId, String driverId, LocalDateTime startTime) {
        this.id = TSID.Factory.getTsid().toString();
        this.routeId = routeId;
        this.driverId = driverId;
        this.startTime = startTime;
    }
}
