package com.movesmart.busdatamanager.vehicle.domain.vehicle;

public interface VehicleManagementUseCase {
    String VEHICLE = "Vehicle";

    /**
     * Creates a new Vehicle
     * @param vehicle data
     * @return Vehicle
     */
    Vehicle create(Vehicle vehicle);

    /**
     * Retrieves a Vehicle using its plate number
     * @param plateNumber Identifier of the Vehicle
     * @return Vehicle
     */
    Vehicle get(String plateNumber);
}
