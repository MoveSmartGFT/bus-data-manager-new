package com.moveSmart.busDataManager.route.domain.stop;

import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;

import java.util.List;

public interface StopManagementUseCase {

    String STOP = "Stop";

    /**
     * Creates a new Stop
     * @param stopRequest data
     * @return Stop
     */
    Stop create(StopRequest stopRequest);

    /**
     * Retrieves a Stop using its id
     * @param stopId Stop identifier
     * @return Stop
     */
    Stop get(String stopId);

    /**
     * Retrieves all Stops
     * @return List of Stop
     */
    List<Stop> getAll();

    /**
     * Updates a Stop
     * @param stop new stop
     * @return Stop
     */
    Stop update(Stop stop);
}
