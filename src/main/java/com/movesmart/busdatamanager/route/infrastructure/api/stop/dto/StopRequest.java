package com.movesmart.busdatamanager.route.infrastructure.api.stop.dto;

import com.movesmart.busdatamanager.route.domain.Coordinates;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StopRequest(@NotBlank String name, @NotNull @Valid Coordinates location) {

    public Stop toStop(String id) {
        return new Stop(id, name, location);
    }

    public Stop toStop() {
        String id = "MS" + name.toUpperCase().replace(" ", "_");

        return new Stop(id, name, location);
    }
}
