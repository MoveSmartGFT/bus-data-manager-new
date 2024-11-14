package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Identity;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Generated
public class VehicleHistory {
    @Identity
    @NotBlank
    private String id;

    @NotBlank
    private String routeId;

    @Valid
    private Driver driver;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
}
