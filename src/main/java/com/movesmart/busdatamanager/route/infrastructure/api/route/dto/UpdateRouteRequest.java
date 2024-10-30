package com.movesmart.busdatamanager.route.infrastructure.api.route.dto;

import com.movesmart.busdatamanager.route.domain.schedule.Schedule;
import com.movesmart.busdatamanager.route.domain.route.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateRouteRequest(@NotBlank String name, @NotNull List<String> stopIds, @NotNull List<Schedule> schedules) {

    public Route toRoute(String id) {
        return new Route(id, name, stopIds, schedules);
    }
}
