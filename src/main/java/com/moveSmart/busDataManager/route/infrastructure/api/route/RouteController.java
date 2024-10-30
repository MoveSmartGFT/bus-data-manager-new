package com.moveSmart.busDataManager.route.infrastructure.api.route;

import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(RouteController.ROUTE_PATH)
@Slf4j
@RequiredArgsConstructor
public class RouteController {
    public static final String ROUTE_PATH = "/api/v1/routes"; // NO SONAR
    public static final String ROUTE_ID_PATH = "/{routeId}"; // NO SONAR
    public static final String STOPS_PATH = "/stops"; // NO SONAR

    private final RouteManagementUseCase routeManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Route create(@Valid @RequestBody Route route) {
        log.info("Route creation is requested");
        return routeManagementUseCase.create(route);
    }

    @GetMapping(ROUTE_ID_PATH+STOPS_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public List<String> getStopIdsByRouteId(@PathVariable String routeId) {
        log.info("Requested stopIds from routeId {}", routeId);
        return routeManagementUseCase.getStopIdsByRouteId(routeId);
    }

    @GetMapping(ROUTE_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Route get(@NotBlank @PathVariable String routeId) {
        log.info("Requested route with id {}", routeId);

        return routeManagementUseCase.get(routeId);
    }

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<Route> getAll() {
        return routeManagementUseCase.getAll();
    }

    @PutMapping(ROUTE_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Route update(@PathVariable String routeId,
                       @Valid @RequestBody UpdateRouteRequest routeRequest) {
        log.info("Requested update route with id {}", routeId);
        return routeManagementUseCase.update(routeRequest.toRoute(routeId));
    }

}

