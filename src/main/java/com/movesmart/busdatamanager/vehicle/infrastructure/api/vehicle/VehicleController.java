package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle;

import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(VehicleController.VEHICLE_PATH)
@Slf4j
@RequiredArgsConstructor
public class VehicleController {
    public static final String VEHICLE_PATH = "/api/v1/vehicle"; // NOSONAR
    public static final String VEHICLE_ID_PATH = "/{vehicleId}"; // NOSONAR

    private final VehicleManagementUseCase vehicleManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody VehicleRequest vehicleRequest) {
        log.info("Vehicle creation is requested");
        return VehicleResponse.fromVehicle(vehicleManagementUseCase.create(vehicleRequest.toVehicle()));
    }
}

