package com.moveSmart.busDataManager.route.infrastructure.api.route;

import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(RouteController.ROUTE_PATH)
@Slf4j
@RequiredArgsConstructor

public class RouteController {
    public static final String ROUTE_PATH = "/api/v1/routes";
    public static final String ROUTE_ID_PATH = "/{routeId}";

    private final RouteManagementUseCase routeManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Route create(@Valid @RequestBody Route route) {
        log.info("Route creation is requested");
        return routeManagementUseCase.create(route);
    }

}
