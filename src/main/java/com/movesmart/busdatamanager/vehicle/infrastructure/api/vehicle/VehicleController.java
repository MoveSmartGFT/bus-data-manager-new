package com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle;

import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
    public static final String VEHICLE_ID_PATH = "/{plateNumber}"; // NOSONAR

    private final VehicleManagementUseCase vehicleManagementUseCase;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code = HttpStatus.CREATED)
    public VehicleResponse create(@Valid @RequestBody VehicleRequest vehicleRequest) {
        log.info("Vehicle creation is requested");
        return VehicleResponse.fromVehicle(vehicleManagementUseCase.create(vehicleRequest.toVehicle()));
    }

    @GetMapping(VEHICLE_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleResponse get(@NotBlank @PathVariable String plateNumber) {
        log.info("Requested vehicle with plate number {}", plateNumber);
        return VehicleResponse.fromVehicle(vehicleManagementUseCase.get(plateNumber));
    }

    @DeleteMapping(VEHICLE_ID_PATH)
    @ResponseStatus(code = HttpStatus.OK)
    public VehicleResponse delete(@PathVariable String plateNumber) {
        log.info("Requested delete vehicle with plate number {}", plateNumber);
        return VehicleResponse.fromVehicle(vehicleManagementUseCase.delete(plateNumber));
    }
}
