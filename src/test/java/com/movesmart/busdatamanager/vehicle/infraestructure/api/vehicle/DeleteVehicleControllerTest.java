package com.movesmart.busdatamanager.vehicle.infraestructure.api.vehicle;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
import net.javacrumbs.jsonunit.core.Option;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class DeleteVehicleControllerTest {

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
            "GIVEN a vehicle delete request is received WHEN the vehicle exists THEN returns vehicle object deleted and status 200")
    void testDelete() throws Exception {
        VehicleResponse vehicleResponse = VehicleResponse.fromVehicle(vehicle);

        when(vehicleManagementUseCase.delete(any())).thenReturn(vehicle);

        mockMvc.perform(delete(
                        VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, vehicle.getPlateNumber()))
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(vehicleResponse)));
    }

    @Test
    @DisplayName("GIVEN a vehicle delete request is received WHEN the vehicle does not exist THEN returns status 404")
    void testDeleteVehicleDoesNotExist() throws Exception {
        when(vehicleManagementUseCase.delete(any()))
                .thenThrow(new EntityNotFoundException("Vehicle", vehicle.getPlateNumber()));

        mockMvc.perform(delete(
                        VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, vehicle.getPlateNumber()))
                .andExpect(status().isNotFound());
    }
}
