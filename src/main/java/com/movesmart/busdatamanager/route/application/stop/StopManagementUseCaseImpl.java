package com.movesmart.busdatamanager.route.application.stop;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movesmart.busdatamanager.route.domain.stop.StopRepository;
import jakarta.annotation.Resource;
import java.util.List;
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
        log.info("Attempting to create Stop with id: {}", stop.getId());

        if (stopRepository.findById(stop.getId()).isPresent()) {
            log.warn("Stop with id {} already exists", stop.getId());
            throw new EntityAlreadyExistsException(STOP, stop.getId());
        }

        Stop savedStop = stopRepository.save(stop);

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

    /**
     * @see StopManagementUseCase#delete(String)
     */
    public Stop delete(String stopId) {
        log.info("Attempting to delete Stop with id: {}", stopId);

        Stop stop = get(stopId);

        log.info("Deleting Stop with id: {}", stopId);
        stopRepository.delete(stop);

        return stop;
    }
}
