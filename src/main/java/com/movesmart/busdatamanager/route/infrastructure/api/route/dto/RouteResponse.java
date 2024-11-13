package com.movesmart.busdatamanager.route.infrastructure.api.route.dto;

import com.movesmart.busdatamanager.route.domain.Schedule;
import com.movesmart.busdatamanager.route.domain.route.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RouteResponse(@NotBlank String id, @NotBlank String name, @NotNull List<String> stopIds, @NotNull List<Schedule> schedules, @NotNull
                            Route.Status status) {
    public static RouteResponse fromRoute(Route route) {
        return new RouteResponse(route.getId(), route.getName(), route.getStopIds(), route.getSchedules(), route.getStatus());
    }
}
