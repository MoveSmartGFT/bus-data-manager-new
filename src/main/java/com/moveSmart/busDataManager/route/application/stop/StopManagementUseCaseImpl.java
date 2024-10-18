package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StopManagementUseCaseImpl implements StopManagementUseCase {

    private final StopRepository stopRepository;

    public StopManagementUseCaseImpl(StopRepository stopRepository) {
        this.stopRepository = stopRepository;
    }

    /**
     * @param stop data
     * @return Stop
     */
    @Override
    public Stop create(Stop stop) {
        if (stopRepository.existsById(stop.getId()))
            throw new EntityAlreadyExistsException(STOP, stop.getId());

        return stopRepository.insert(stop);
    }

    /**
     * @see StopManagementUseCase#get(String)
     */
    public Stop get(String stopId) {
        return stopRepository.findById(stopId).orElseThrow(() -> new EntityNotFoundException(STOP, stopId));
    }
}
