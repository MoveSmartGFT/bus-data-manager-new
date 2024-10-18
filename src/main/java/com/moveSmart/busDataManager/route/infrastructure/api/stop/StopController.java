package com.moveSmart.busDataManager.route.infrastructure.api.stop;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.route.RouteController;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(StopController.STOP_PATH)
@Slf4j
public class StopController {

    public static final String STOP_PATH =
            RouteController.ROUTE_PATH + "/stops";
    public static final String STOP_ID_PATH = "/{stopId}";

    private final StopManagementUseCase stopManagementUseCase;

    public StopController(StopManagementUseCase stopManagementUseCase) {
        this.stopManagementUseCase = stopManagementUseCase;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Stop create(@Valid @RequestBody Stop stop) {
        return stopManagementUseCase.create(stop);
    }

    @GetMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Stop get(@Valid @PathVariable String stopId) {
        return stopManagementUseCase.get(stopId);
    }
}