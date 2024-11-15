package com.movesmart.busdatamanager.route.infrastructure.api.stop.dto;

import com.movesmart.busdatamanager.route.domain.Coordinates;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StopResponse(
        @NotBlank String id, @NotBlank String name, @NotNull @Valid Coordinates location, @NotNull Stop.Status status) {
    public static StopResponse fromStop(Stop stop) {
        return new StopResponse(stop.getId(), stop.getName(), stop.getLocation(), stop.getStatus());
    }
}
