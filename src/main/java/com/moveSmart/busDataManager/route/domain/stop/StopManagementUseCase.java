package com.moveSmart.busDataManager.route.domain.stop;

public interface StopManagementUseCase {

    String STOP = "Stop";

    /**
     * Creates a new Stop
     * @param stop data
     * @return Stop
     */
    Stop create(Stop stop);
}
