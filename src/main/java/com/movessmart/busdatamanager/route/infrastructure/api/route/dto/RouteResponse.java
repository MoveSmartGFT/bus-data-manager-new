package com.movessmart.busdatamanager.route.infrastructure.api.route.dto;

import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.infrastructure.api.model.ScheduleDTO;
import java.util.List;
import java.util.stream.Collectors;

public record RouteResponse(
        String id, String name, List<String> stopIds, List<ScheduleDTO> schedules, Route.Status status) {
    public static RouteResponse fromRoute(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getName(),
                route.getStopIds(),
                route.getSchedules().stream().map(ScheduleDTO::fromSchedule).collect(Collectors.toList()),
                route.getStatus());
    }
}
