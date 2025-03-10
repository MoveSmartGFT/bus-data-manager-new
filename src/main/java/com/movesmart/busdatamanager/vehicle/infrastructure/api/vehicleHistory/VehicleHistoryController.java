package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory;

import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryManagmentUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VehicleHistoryController.VEHICLE_HISTORY_PATH)
@Slf4j
@RequiredArgsConstructor
public class VehicleHistoryController {
    public static final String VEHICLE_HISTORY_PATH = "/api/v1/vehicle-history"; // NOSONAR

    private final VehicleHistoryManagmentUseCase vehicleHistoryManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleHistoryResponse create(@Valid @RequestBody VehicleHistoryRequest vehicleHistoryRequest) {
        log.info("VehicleHistory creation is requested");
        return VehicleHistoryResponse.fromVehicleHistory(
                vehicleHistoryManagementUseCase.create(vehicleHistoryRequest.toVehicleHistory()));
    }
}
