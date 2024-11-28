package com.movesmart.busdatamanager.vehicle.domain.vehicleHistory;

public interface VehicleHistoryManagmentUseCase {

    String VEHICLE_HISTORY = "VehicleHistory";

    /**
     * Creates a new vehicleHistory
     * @param vehicleHistory data
     * @return vehicleHistory
     */
    VehicleHistory create(VehicleHistory vehicleHistory);
}
