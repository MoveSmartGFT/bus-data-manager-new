package com.movessmart.busdatamanager.vehicle.domain.vehicle;

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

    /**
     * Deletes a Vehicle
     * @param plateNumber to delete
     * @return Vehicle
     */
    Vehicle delete(String plateNumber);

    /**
     * Updates a Vehicle
     * @param vehicle new vehicle
     * @return Vehicle
     */
    Vehicle update(Vehicle vehicle);
}
