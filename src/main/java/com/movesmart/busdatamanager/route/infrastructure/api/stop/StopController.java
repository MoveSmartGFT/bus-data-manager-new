package com.movesmart.busdatamanager.route.infrastructure.api.stop;

import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopResponse;
import jakarta.validation.Valid;
import java.util.List;
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

    public static final String STOP_PATH = "/api/v1/stops"; // NOSONAR
    public static final String STOP_ID_PATH = "/{stopId}"; // NOSONAR
    public static final String ROUTE_PATH = "/routes"; // NOSONAR
    public static final String STOP_DISABLE_PATH = "/disable"; // NOSONAR
    public static final String STOP_ENABLE_PATH = "/enable"; // NOSONAR

    private final StopManagementUseCase stopManagementUseCase;
    private final RouteManagementUseCase routeManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public StopResponse create(@Valid @RequestBody StopRequest stopRequest) {
        log.info("Stop creation is requested");
        return StopResponse.fromStop(stopManagementUseCase.create(stopRequest.toStop()));
    }

    @GetMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public StopResponse get(@PathVariable String stopId) {
        log.info("Requested stop with id {}", stopId);
        return StopResponse.fromStop(stopManagementUseCase.get(stopId));
    }

    @GetMapping()
    @ResponseStatus(code = HttpStatus.OK)
    public List<StopResponse> getAll() {
        return stopManagementUseCase.getAll().stream()
                .map(StopResponse::fromStop)
                .toList();
    }

    @PutMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public StopResponse update(@PathVariable String stopId, @Valid @RequestBody StopRequest stopRequest) {
        log.info("Requested update stop with id {}", stopId);
        return StopResponse.fromStop(stopManagementUseCase.update(stopRequest.toStop(stopId)));
    }

    @PatchMapping(STOP_ID_PATH + ROUTE_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public String removeStopIdFromRoutes(@PathVariable String stopId) {
        log.info("Requested delete stop with id {} from all routes", stopId);
        return routeManagementUseCase.removeStopIdFromRoutes(stopId);
    }

    @PatchMapping(STOP_ID_PATH + STOP_DISABLE_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public StopResponse disable(@PathVariable String stopId) {
        log.info("Requested disable stop with id {}", stopId);
        return StopResponse.fromStop(stopManagementUseCase.disable(stopId));
    }

    @PatchMapping(STOP_ID_PATH + STOP_ENABLE_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public StopResponse enable(@PathVariable String stopId) {
        log.info("Requested enable stop with id {}", stopId);
        return StopResponse.fromStop(stopManagementUseCase.enable(stopId));
    }

    @DeleteMapping(STOP_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public StopResponse delete(@PathVariable String stopId) {
        log.info("Requested delete stop with id {}", stopId);
        return StopResponse.fromStop(stopManagementUseCase.delete(stopId));
    }
}
