package com.movesmart.busdatamanager.route.infrastructure.api.stop.dto;

import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.model.CoordinatesDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StopResponse(
        @NotBlank String id,
        @NotBlank String name,
        @NotNull @Valid CoordinatesDTO location,
        @NotNull Stop.Status status) {
    public static StopResponse fromStop(Stop stop) {
        return new StopResponse(
                stop.getId(), stop.getName(), CoordinatesDTO.fromCoordinates(stop.getLocation()), stop.getStatus());
    }
}
