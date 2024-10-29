package com.moveSmart.busDataManager.route.infrastructure.api.stop;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public Stop create(@Valid @RequestBody StopRequest createStopRequest) {
        log.info("Stop creation is requested");
        return stopManagementUseCase.create(createStopRequest);
    }

    @GetMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Stop get(@PathVariable String stopId) {
        log.info("Requested stop with id {}", stopId);
        return stopManagementUseCase.get(stopId);
    }

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<Stop> getAll() {
        return stopManagementUseCase.getAll();
    }

    @PutMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public Stop update(@PathVariable String stopId,
                       @Valid @RequestBody StopRequest stopRequest) {
        log.info("Requested update stop with id {}", stopId);
        return stopManagementUseCase.update(stopRequest.toStop(stopId));
    }
}