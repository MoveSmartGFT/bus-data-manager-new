package com.movesmart.busdatamanager.vehicle.application.vehicle;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
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
        log.info("Attempting to create Vehicle with id: {}", vehicle.getId());

        if (vehicleRepository.existsById(vehicle.getId())) {
            log.warn("Vehicle with id: {} already exists", vehicle.getId());
            throw new EntityAlreadyExistsException(VEHICLE, vehicle.getId());
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle with ID: {} successfully created", vehicle.getId());

        return savedVehicle;
    }
}
