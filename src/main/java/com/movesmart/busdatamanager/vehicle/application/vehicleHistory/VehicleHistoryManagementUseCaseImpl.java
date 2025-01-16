package com.movesmart.busdatamanager.vehicle.application.vehicleHistory;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.vehicle.VehicleSender;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryManagmentUseCase;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryRepository;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleHistoryManagementUseCaseImpl implements VehicleHistoryManagmentUseCase {
    @Resource
    private final VehicleHistoryRepository vehicleHistoryRepository;

    @Resource
    private final VehicleRepository vehicleRepository;

    private final VehicleSender send;

    private final ApplicationEventPublisher eventPublisher;

    /**
     * @param vehicleHistory data
     * @return VehicleHistory
     */
    @CacheEvict(value = "VehicleHistory", beforeInvocation = true)
    @Override
    public VehicleHistory create(VehicleHistory vehicleHistory) {
        log.info("Attempting to create vehicleHistory with id: {}", vehicleHistory.getId());

        if (vehicleHistoryRepository.findById(vehicleHistory.getId()).isPresent()) {
            log.warn("vehicleHistory with id {} already exists", vehicleHistory.getId());
            throw new EntityAlreadyExistsException(VEHICLE_HISTORY, vehicleHistory.getId());
        }

        checkVehicleExist(vehicleHistory.getVehicleId());
        // checkRouteExist(vehicleHistory.getRouteId());

        VehicleHistory savedVehicleHistory = vehicleHistoryRepository.save(vehicleHistory);
        String message = String.format("VehicleHistory created with ID: %s", savedVehicleHistory.getId());
        send.sendMessage(message);

        log.info("VehicleHistory successfully created with id: {}", savedVehicleHistory.getId());

        return savedVehicleHistory;
    }

    private void checkVehicleExist(String vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            log.warn("Vehicle with id: {} does not exists", vehicleId);
            throw new EntityNotFoundException("Vehicle", vehicleId);
        }
    }

    //    private void checkRouteExist(String routeId) {
    //        RouteValidationEvent event = new RouteValidationEvent(routeId, false);
    //        eventPublisher.publishEvent(event);
    //
    //        if (!event.isValidated()) {
    //            throw new EntityNotFoundException("Route", routeId);
    //        }
    //    }
}
