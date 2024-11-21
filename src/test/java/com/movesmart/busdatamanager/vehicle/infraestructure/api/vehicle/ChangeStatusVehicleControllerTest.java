package com.movesmart.busdatamanager.vehicle.infraestructure.api.vehicle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleManagementUseCase;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.ChangeStatusVehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class ChangeStatusVehicleControllerTest {
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
            "GIVEN a vehicle change status request is received WHEN the vehicle exists THEN returns vehicle object with the status changed and status 200")
    void testChangeStatus() throws Exception {
        Vehicle.Status newStatus = Vehicle.Status.OutOfService;
        ChangeStatusVehicleRequest changeStatusRequest = new ChangeStatusVehicleRequest(vehicle.getPlateNumber(), newStatus);

        when(vehicleManagementUseCase.changeStatus(eq(vehicle.getPlateNumber()), eq(newStatus))).thenReturn(vehicle);

        mockMvc.perform(patch(
                VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH + VehicleController.VEHICLE_CHANGE_STATUS_PATH, vehicle.getPlateNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeStatusRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GIVEN a vehicle change status request is received WHEN the vehicle does not exist THEN returns status 404")
    void testChangeStatusVehicleDoesNotExist() throws Exception {
        Vehicle.Status newStatus = Vehicle.Status.OutOfService;

        when(vehicleManagementUseCase.changeStatus(eq("Vehicle1"), eq(newStatus)))
                .thenThrow(new EntityNotFoundException("Vehicle", "Vehicle1"));

        mockMvc.perform(patch(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH + VehicleController.VEHICLE_CHANGE_STATUS_PATH, "Vehicle1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ChangeStatusVehicleRequest("Vehicle1", newStatus))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a vehicle change status request is received WHEN the vehicle already has that status THEN returns status 409")
    void testChangeStatusVehicleAlreadyEnabled() throws Exception {
        Vehicle.Status newStatus = Vehicle.Status.OutOfService;

        ChangeStatusVehicleRequest changeStatusRequest = new ChangeStatusVehicleRequest(vehicle.getPlateNumber(), newStatus);

        when(vehicleManagementUseCase.changeStatus(eq(vehicle.getPlateNumber()), eq(newStatus)))
                .thenThrow(new EntityStatusException("Vehicle", vehicle.getPlateNumber(), newStatus.toString()));

        mockMvc.perform(patch(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH + VehicleController.VEHICLE_CHANGE_STATUS_PATH, vehicle.getPlateNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeStatusRequest)))
                .andExpect(status().isNotFound());
    }
}
