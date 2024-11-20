package com.movesmart.busdatamanager.vehicle.infraestructure.api.vehicle;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movessmart.busdatamanager.core.Fixtures;
import com.movessmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movessmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
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
public class CreateVehicleControllerTest {

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
    @DisplayName("WHEN a vehicle creation request is received THEN returns vehicle object and status 201")
    void testVehicleCreate() throws Exception {
        VehicleResponse vehicleResponse = VehicleResponse.fromVehicle(vehicle);

        when(vehicleManagementUseCase.create(any())).thenReturn(vehicle);

        mockMvc.perform(post(VehicleController.VEHICLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isCreated())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(vehicleResponse)));
    }

    @Test
    @DisplayName("WHEN a vehicle creation request is received WHEN vehicle already exists THEN returns status 409")
    void testVehicleCreateConflict() throws Exception {
        when(vehicleManagementUseCase.create(any()))
                .thenThrow(new EntityAlreadyExistsException("Vehicle", vehicle.getPlateNumber()));

        mockMvc.perform(post(VehicleController.VEHICLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("WHEN a vehicle creation request is received WHEN is a bad request THEN returns status 400")
    void testVehicleCreateBadRequest() throws Exception {
        mockMvc.perform(post(VehicleController.VEHICLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("Vehicle"))
                .andExpect(status().isBadRequest());
    }
}
