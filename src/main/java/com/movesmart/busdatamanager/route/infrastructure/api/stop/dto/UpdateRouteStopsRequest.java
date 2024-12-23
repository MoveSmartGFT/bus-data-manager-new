package com.movesmart.busdatamanager.route.infrastructure.api.stop.dto;

import com.movesmart.busdatamanager.route.domain.route.Route;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateRouteStopsRequest(@NotNull List<String> stopIds) {
    public Route toRoute(Route route) {
        return new Route(route.getId(), route.getName(), stopIds, route.getSchedules());
    }
}
