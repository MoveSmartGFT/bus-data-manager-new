package com.moveSmart.busDataManager.route.domain.stop;

public interface StopManagementUseCase {

    String STOP = "Stop";

    /**
     * Creates a new Stop
     * @param stop data
     * @return Stop
     */
    Stop create(Stop stop);

    /**
     * Retrieves a Stop using its Id
     * @param stopId Stop identifier
     * @return Stop
     */
    Stop get(String stopId);
}
