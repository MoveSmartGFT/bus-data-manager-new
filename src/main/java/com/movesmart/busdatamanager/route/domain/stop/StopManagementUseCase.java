package com.movesmart.busdatamanager.route.domain.stop;

import java.util.List;

public interface StopManagementUseCase {

    String STOP = "Stop";

    /**
     * Creates a new Stop
     * @param stop data
     * @return Stop
     */
    Stop create(Stop stop);

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
