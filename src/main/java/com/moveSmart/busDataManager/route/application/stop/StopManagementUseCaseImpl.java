package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class StopManagementUseCaseImpl implements StopManagementUseCase {

    @Resource
    private final StopRepository stopRepository;

    /**
     * @param stop data
     * @return Stop
     */
    @Override
    public Stop create(Stop stop) {
        log.info("Attempting to create Stop with ID: {}", stop.getId());

        if (stopRepository.existsById(stop.getId())) {
            log.warn("Stop with ID: {} already exists", stop.getId());
            throw new EntityAlreadyExistsException(STOP, stop.getId());
        }

        Stop savedStop = stopRepository.save(stop);
        log.info("Stop with ID: {} successfully created", stop.getId());

        return savedStop;
    }

    /**
     * @see StopManagementUseCase#get(String)
     */
    public Stop get(String stopId) {
        return stopRepository.findById(stopId).orElseThrow(() -> new EntityNotFoundException(STOP, stopId));
    }
}