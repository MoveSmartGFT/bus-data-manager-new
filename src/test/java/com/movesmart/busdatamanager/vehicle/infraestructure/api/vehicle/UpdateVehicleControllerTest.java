package com.movesmart.busdatamanager.vehicle.infraestructure.api.vehicle;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import net.javacrumbs.jsonunit.core.Option;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UpdateVehicleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleManagementUseCase vehicleManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final VehicleRequest vehicleRequest = Instancio.create(VehicleInstancioModels.VEHICLE_REQUEST_MODEL);
    private final Vehicle vehicle = vehicleRequest.toVehicle();

    @BeforeEach
    void setUp() {
        VehicleController vehicleController = new VehicleController(vehicleManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(vehicleController);
    }

    @Test
    @DisplayName(
            "GIVEN a vehicle update request is received WHEN the vehicle exists THEN returns vehicle object updated and status 200")
    void testUpdate() throws Exception {
        VehicleRequest newVehicle = Instancio.create(VehicleInstancioModels.VEHICLE_REQUEST_MODEL);

        when(vehicleManagementUseCase.update(any())).thenReturn(vehicle);

        mockMvc.perform(put(
                VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, vehicle.getPlateNumber())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newVehicle)))
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(vehicle)));
    }

    @Test
    @DisplayName("GIVEN a vehicle update request is received WHEN the vehicle does not exist THEN returns status 404")
    void testUpdateVehicleDoesNotExist() throws Exception {
        VehicleRequest newVehicle = Instancio.create(VehicleInstancioModels.VEHICLE_REQUEST_MODEL);

        when(vehicleManagementUseCase.update(any()))
                .thenThrow(new EntityNotFoundException("Vehicle", vehicle.getPlateNumber()));

        mockMvc.perform(put(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, vehicle.getPlateNumber())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newVehicle)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a vehicle update request is received WHEN is a bad request THEN returns status 400")
    void testUpdateVehicleBadRequest() throws Exception {
        mockMvc.perform(put(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, vehicle.getPlateNumber())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("Vehicle")))
                .andExpect(status().isBadRequest());
    }
}
