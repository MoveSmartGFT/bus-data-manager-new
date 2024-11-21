package com.movesmart.busdatamanager.vehicle.application.vehicle;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "vehicle")
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
    @Cacheable(value = "vehicle", key = "#plateNumber")
    @Override
    public Vehicle get(String plateNumber) {
        log.info("Searching vehicle with plate number: {}", plateNumber);

        return vehicleRepository
                .findById(plateNumber)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE, plateNumber));
    }

    /**
     * @see VehicleManagementUseCase#delete(String)
     */
    @CacheEvict(value = "vehicle", key = "#plateNumber", allEntries = true)
    @Override
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
    @CachePut(value = "vehicle", key = "#vehicle.plateNumber")
    public Vehicle update(Vehicle vehicle) {
        log.info("Attempting to update Vehicle with id: {}", vehicle.getPlateNumber());

        get(vehicle.getPlateNumber());

        log.info("Found Vehicle with id: {}", vehicle.getPlateNumber());
        return vehicleRepository.save(vehicle);
    }

    /**
     * @see VehicleManagementUseCase#changeStatus(String, Vehicle.Status)
     */
    @CacheEvict(value = "vehicle", key = "#plateNumber", allEntries = true)
    @Override
    public Vehicle changeStatus(String plateNumber, Vehicle.Status newStatus) {
        log.info("Attempting to change the status of the Vehicle with id: {}", plateNumber);

        Vehicle vehicle = vehicleRepository
                .findById(plateNumber)
                .orElseThrow(() -> new EntityNotFoundException(VEHICLE, plateNumber));

        if (vehicle.getStatus() == newStatus) {
            throw new EntityStatusException("Vehicle", plateNumber, newStatus.toString());
        }

        log.info("Changing status of Vehicle with plate number: {} to {}", plateNumber, newStatus);

        updateVehicleStatus(vehicle, newStatus);

        return vehicleRepository.save(vehicle);
    }

    private void updateVehicleStatus(Vehicle vehicle, Vehicle.Status newStatus) {
        switch (newStatus) {
            case InService -> vehicle.setVehicleInService();
            case OutOfService -> vehicle.setVehicleOutOfService();
            case InMaintenance -> vehicle.setVehicleInMaitenance();
        }
    }
}
