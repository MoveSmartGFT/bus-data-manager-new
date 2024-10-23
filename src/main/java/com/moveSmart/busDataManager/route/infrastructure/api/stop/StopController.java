package com.moveSmart.busDataManager.route.infrastructure.api.stop;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(StopController.STOP_PATH)
@Slf4j
@RequiredArgsConstructor
public class StopController {

    public static final String STOP_PATH = "/api/v1/stops";
    public static final String STOP_ID_PATH = "/{stopId}";

    private final StopManagementUseCase stopManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public Stop create(@Valid @RequestBody Stop stop) {
        log.info("Stop creation is requested");
        return stopManagementUseCase.create(stop);
    }

    @GetMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Stop get(@NotBlank @PathVariable String stopId) {
        log.info("Requested stop with id {}", stopId);
        return stopManagementUseCase.get(stopId);
    }
}