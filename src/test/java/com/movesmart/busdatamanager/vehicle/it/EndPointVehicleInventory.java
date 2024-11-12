package com.movesmart.busdatamanager.vehicle.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.VehicleController;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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
        return this.mockMvc.perform(
                        post(VehicleController.VEHICLE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(vehicleRequest))
                )
                .andReturn();
    }
}
