package com.moveSmart.busDataManager.route.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.StopController;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ApplicationModuleTest
@AutoConfigureMockMvc
public abstract class EndPointStopInventory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected MvcResult createStopRequest(StopRequest stopRequest) throws Exception {
        return this.mockMvc.perform(
                        post(StopController.STOP_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(stopRequest))
                )
                .andReturn();
    }

    protected MvcResult getStopRequest(String stopId) throws Exception {
        return this.mockMvc.perform(
                        get(StopController.STOP_PATH+StopController.STOP_ID_PATH, stopId)
                )
                .andReturn();
    }

    protected MvcResult updateStopRequest(String stopId, StopRequest stop) throws Exception {
        return this.mockMvc.perform(
                        put(StopController.STOP_PATH+StopController.STOP_ID_PATH, stopId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(stop))
                )
                .andReturn();
    }
}
