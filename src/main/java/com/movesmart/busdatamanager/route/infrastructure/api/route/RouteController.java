package com.movesmart.busdatamanager.route.infrastructure.api.route;

import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteStopsRequest;
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
    public static final String ROUTE_PATH = "/api/v1/routes"; // NOSONAR
    public static final String ROUTE_ID_PATH = "/{routeId}"; // NOSONAR
    public static final String ROUTE_DISABLE_PATH = "/disable"; // NOSONAR
    public static final String ROUTE_ENABLE_PATH = "/enable"; // NOSONAR
    public static final String STOPS_PATH = "/stops"; // NOSONAR

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

    @PatchMapping(ROUTE_ID_PATH+ROUTE_DISABLE_PATH)
    @ResponseStatus(code =  HttpStatus.OK)
    public Route disable(@PathVariable String routeId) {
        log.info("Requested disable route with id {}", routeId);
        return routeManagementUseCase.disable(routeId);
    }

    @PatchMapping(ROUTE_ID_PATH+ROUTE_ENABLE_PATH)
    @ResponseStatus(code =  HttpStatus.OK)
    public Route enable(@PathVariable String routeId) {
        log.info("Requested enable route with id {}", routeId);
        return routeManagementUseCase.enable(routeId);
    }

    @DeleteMapping(ROUTE_ID_PATH)
    @ResponseStatus(code =  HttpStatus.OK)
    public Route delete(@PathVariable String routeId) {
        log.info("Requested delete route with id {}", routeId);
        return routeManagementUseCase.delete(routeId);
    }

    @PatchMapping(ROUTE_ID_PATH + STOPS_PATH)
    @ResponseStatus(code =  HttpStatus.OK)
    public Route updateStopsRoute(@PathVariable String routeId,
                                  @Valid @RequestBody UpdateRouteStopsRequest routeRequest){
        log.info("Requested update stops of the route with id {}", routeId);
        Route existingRoute = routeManagementUseCase.get(routeId);
        Route updatedRoute = routeRequest.toRoute(existingRoute);
        return routeManagementUseCase.updateRouteStops(updatedRoute);
    }
}

