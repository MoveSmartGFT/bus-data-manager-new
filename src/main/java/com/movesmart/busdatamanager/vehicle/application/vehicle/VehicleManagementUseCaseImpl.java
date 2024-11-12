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
        log.info("Attempting to create Vehicle with plate number: {}", vehicle.getPlateNumber());

        if (vehicleRepository.existsById(vehicle.getPlateNumber())) {
            log.warn("Vehicle with plate number: {} already exists", vehicle.getPlateNumber());
            throw new EntityAlreadyExistsException(VEHICLE, vehicle.getPlateNumber());
        }

        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle with plate number: {} successfully created", vehicle.getPlateNumber());

        return savedVehicle;
    }
}
