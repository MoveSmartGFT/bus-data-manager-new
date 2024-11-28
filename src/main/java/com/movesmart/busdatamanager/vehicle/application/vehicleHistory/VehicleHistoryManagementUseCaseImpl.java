package com.movesmart.busdatamanager.vehicle.application.vehicleHistory;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryManagmentUseCase;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleHistoryManagementUseCaseImpl implements VehicleHistoryManagmentUseCase {
    @Resource
    private final VehicleHistoryRepository vehicleHistoryRepository;

    /**
     * @param vehicleHistory data
     * @return VehicleHistory
     */
    @CacheEvict(value = "VehicleHistory")
    @Override
    public VehicleHistory create(VehicleHistory vehicleHistory) {
        log.info("Attempting to create vehicleHistory with id: {}", vehicleHistory.getId());

        if (vehicleHistoryRepository.findById(vehicleHistory.getId()).isPresent()) {
            log.warn("vehicleHistory with id {} already exists", vehicleHistory.getId());
            throw new EntityAlreadyExistsException(VEHICLE_HISTORY, vehicleHistory.getId());
        }

        VehicleHistory savedVehicleHistory = vehicleHistoryRepository.save(vehicleHistory);

        log.info("VehicleHistory successfully created with id: {}", savedVehicleHistory.getId());

        return savedVehicleHistory;
    }
}
