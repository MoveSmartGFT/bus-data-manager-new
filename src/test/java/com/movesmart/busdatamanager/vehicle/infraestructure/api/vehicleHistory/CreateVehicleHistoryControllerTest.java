package com.movesmart.busdatamanager.vehicle.infraestructure.api.vehicleHistory;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryManagmentUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.VehicleHistoryController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryResponse;
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
public class CreateVehicleHistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleHistoryManagmentUseCase vehicleHistoryManagmentUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final VehicleHistoryRequest vehicleHistoryRequest =
            Instancio.create(VehicleInstancioModels.VEHICLE_HISTORY_REQUEST_MODEL);
    private final VehicleHistory vehicleHistory = vehicleHistoryRequest.toVehicleHistory();

    @BeforeEach
    void setUp() {
        VehicleHistoryController vehicleController = new VehicleHistoryController(vehicleHistoryManagmentUseCase);
        mockMvc = Fixtures.setupMockMvc(vehicleController);
    }

    @Test
    @DisplayName("WHEN a vehicleHistory creation request is received THEN returns vehicleHistory object and status 201")
    void testVehicleHistoryCreate() throws Exception {
        VehicleHistoryResponse vehicleHistoryResponse = VehicleHistoryResponse.fromVehicleHistory(vehicleHistory);

        when(vehicleHistoryManagmentUseCase.create(any())).thenReturn(vehicleHistory);

        mockMvc.perform(post(VehicleHistoryController.VEHICLE_HISTORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleHistoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(vehicleHistoryResponse)));
    }

    @Test
    @DisplayName(
            "WHEN a vehicleHistory creation request is received WHEN vehicleHistory already exists THEN returns status 409")
    void testVehicleHistoryCreateConflict() throws Exception {
        when(vehicleHistoryManagmentUseCase.create(any()))
                .thenThrow(new EntityAlreadyExistsException("vehicleHistory", vehicleHistory.getId()));

        mockMvc.perform(post(VehicleHistoryController.VEHICLE_HISTORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleHistoryRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("WHEN a vehicleHistory creation request is received WHEN is a bad request THEN returns status 400")
    void testVehicleRequestCreateBadRequest() throws Exception {
        mockMvc.perform(post(VehicleHistoryController.VEHICLE_HISTORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("VehicleHistory"))
                .andExpect(status().isBadRequest());
    }
}
