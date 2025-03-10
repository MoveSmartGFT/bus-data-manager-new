package com.movesmart.busdatamanager.vehicle.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.ChangeStatusVehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.UpdateVehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.VehicleHistoryController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@ActiveProfiles("test")
@ApplicationModuleTest
@AutoConfigureMockMvc
public abstract class EndPointVehicleInventory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected MvcResult createVehicleRequest(VehicleRequest vehicleRequest) throws Exception {
        return this.mockMvc
                .perform(post(VehicleController.VEHICLE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andReturn();
    }

    protected MvcResult getVehicleRequest(String plateNumber) throws Exception {
        return this.mockMvc
                .perform(get(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, plateNumber))
                .andReturn();
    }

    protected MvcResult deleteVehicleRequest(String plateNumber) throws Exception {
        return this.mockMvc
                .perform(delete(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, plateNumber))
                .andReturn();
    }

    protected MvcResult updateVehicleRequest(String plateNumber, UpdateVehicleRequest vehicle) throws Exception {
        return this.mockMvc
                .perform(put(VehicleController.VEHICLE_PATH + VehicleController.VEHICLE_ID_PATH, plateNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andReturn();
    }

    protected MvcResult changeStatusRequest(String plateNumber, ChangeStatusVehicleRequest vehicle) throws Exception {
        return this.mockMvc
                .perform(patch(
                                VehicleController.VEHICLE_PATH
                                        + VehicleController.VEHICLE_ID_PATH
                                        + VehicleController.VEHICLE_CHANGE_STATUS_PATH,
                                plateNumber)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicle)))
                .andReturn();
    }

    protected MvcResult createVehicleHistoryRequest(VehicleHistoryRequest vehicleHistoryRequest) throws Exception {
        return this.mockMvc
                .perform(post(VehicleHistoryController.VEHICLE_HISTORY_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(vehicleHistoryRequest)))
                .andReturn();
    }
}
