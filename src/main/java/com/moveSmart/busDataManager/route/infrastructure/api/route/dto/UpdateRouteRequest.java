package com.moveSmart.busDataManager.route.infrastructure.api.route.dto;

import com.moveSmart.busDataManager.route.domain.route.Route;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateRouteRequest(@NotBlank String name, @NotNull List<String> stopIds, @NotNull List<String> schedules) {

    public Route toRoute(String id) {
        return new Route(id, name, stopIds, schedules);
    }
}
