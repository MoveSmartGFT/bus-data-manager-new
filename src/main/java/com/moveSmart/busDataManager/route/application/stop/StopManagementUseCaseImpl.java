package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StopManagementUseCaseImpl implements StopManagementUseCase {

    @Resource
    private final StopRepository stopRepository;

    /**
     * @param stopRequest data
     * @return Stop
     */
    @Override
    public Stop create(StopRequest stopRequest) {
        log.info("Attempting to create Stop with name: {}", stopRequest.name());

        if (stopRepository.findByName(stopRequest.name()).isPresent()) {
            log.warn("Stop {} already exists", stopRequest.name());
            throw new EntityAlreadyExistsException(STOP, stopRequest.name());
        }

        Stop savedStop = stopRepository.save(stopRequest.toStop());

        log.info("Stop successfully created with id: {}", savedStop.getId());

        return savedStop;
    }

    /**
     * @see StopManagementUseCase#get(String)
     */
    public Stop get(String stopId) {
        log.info("Searching stop with id: {}", stopId);

        return stopRepository.findById(stopId).orElseThrow(() -> new EntityNotFoundException(STOP, stopId));
    }

    /**
     * @see StopManagementUseCase#getAll()
     */
    public List<Stop> getAll() {
        log.info("Retrieving all stops");

        return stopRepository.findAll();
    }

    /**
     * @see StopManagementUseCase#update(Stop)
     */
    public Stop update(Stop stop) {
        log.info("Attempting to update Stop with id: {}", stop.getId());

        get(stop.getId());

        log.info("Found Stop with id: {}", stop.getId());
        return stopRepository.save(stop);
    }
}