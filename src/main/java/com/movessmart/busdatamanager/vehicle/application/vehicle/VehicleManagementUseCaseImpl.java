package com.movessmart.busdatamanager.vehicle.application.vehicle;

import com.movessmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleManagementUseCaseImpl implements VehicleManagementUseCase {

    private final VehicleRepository vehicleRepository;

    /**
     * @param vehicle data
     * @return Vehicle
     */
    @Override
    public Vehicle create(Vehicle vehicle) {
        log.info("Attempting to create Vehicle with plate number: {}", vehicle.getPlateNumber());

        if (vehicleRepository.existsById(vehicle.getPlateNumber())) {
            log.warn("Vehicle with plate number: {} already exists", vehicle.getPlateNumber());
            throw new EntityAlreadyExistsException(VEHICLE, vehicle.getPlateNumber());
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle with plate number: {} successfully created", vehicle.getPlateNumber());

        return savedVehicle;
    }

    /**
     * @see VehicleManagementUseCase#get(String)
     */
    public Vehicle get(String plateNumber) {
        log.info("Searching vehicle with plate number: {}", plateNumber);

        return vehicleRepository
                .findById(plateNumber)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE, plateNumber));
    }

    /**
     * @see VehicleManagementUseCase#delete(String)
     */
    public Vehicle delete(String plateNumber) {
        log.info("Attempting to delete vehicle with plate number: {}", plateNumber);

        Vehicle vehicle = get(plateNumber);

        log.info("Deleting vehicle with plate number: {}", plateNumber);
        vehicleRepository.delete(vehicle);

        return vehicle;
    }

    /**
     * @see VehicleManagementUseCase#update(Vehicle)
     */
    public Vehicle update(Vehicle vehicle) {
        log.info("Attempting to update Vehicle with id: {}", vehicle.getPlateNumber());

        get(vehicle.getPlateNumber());

        log.info("Found Vehicle with id: {}", vehicle.getPlateNumber());
        return vehicleRepository.save(vehicle);
    }
}