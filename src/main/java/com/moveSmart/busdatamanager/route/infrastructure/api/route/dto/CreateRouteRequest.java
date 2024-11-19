package com.movesmart.busdatamanager.route.infrastructure.api.route.dto;

import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.infrastructure.api.model.ScheduleDTO;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

public record CreateRouteRequest(@NotBlank String name, List<String> stopIds, List<ScheduleDTO> schedules) {
    public Route toRoute() {
        return new Route(
                name, stopIds, schedules.stream().map(ScheduleDTO::toSchedule).collect(Collectors.toList()));
    }
}
