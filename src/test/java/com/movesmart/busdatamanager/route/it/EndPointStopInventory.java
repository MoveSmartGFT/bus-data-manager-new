package com.movesmart.busdatamanager.route.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.StopController;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
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
public abstract class EndPointStopInventory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected MvcResult createStopRequest(StopRequest stopRequest) throws Exception {
        return this.mockMvc
                .perform(post(StopController.STOP_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(stopRequest)))
                .andReturn();
    }

    protected MvcResult getStopRequest(String stopId) throws Exception {
        return this.mockMvc
                .perform(get(StopController.STOP_PATH + StopController.STOP_ID_PATH, stopId))
                .andReturn();
    }

    protected MvcResult getAllStopsRequest() throws Exception {
        return this.mockMvc.perform(get(StopController.STOP_PATH)).andReturn();
    }

    protected MvcResult updateStopRequest(String stopId, StopRequest stop) throws Exception {
        return this.mockMvc
                .perform(put(StopController.STOP_PATH + StopController.STOP_ID_PATH, stopId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(stop)))
                .andReturn();
    }

    protected MvcResult removeStopIdFromRoutesRequest(String stopId) throws Exception {
        return this.mockMvc
                .perform(patch(
                        StopController.STOP_PATH + StopController.STOP_ID_PATH + StopController.ROUTE_PATH, stopId))
                .andReturn();
    }

    protected MvcResult deleteStopRequest(String stopId) throws Exception {
        return this.mockMvc
                .perform(delete(StopController.STOP_PATH + StopController.STOP_ID_PATH, stopId))
                .andReturn();
    }

    protected MvcResult disableStopRequest(String stopId) throws Exception {
        return this.mockMvc.perform(
                        patch(StopController.STOP_PATH+StopController.STOP_ID_PATH+StopController.STOP_DISABLE_PATH, stopId)
                )
                .andReturn();
    }

    protected MvcResult enableStopRequest(String stopId) throws Exception {
        return this.mockMvc.perform(
                        patch(StopController.STOP_PATH+StopController.STOP_ID_PATH+StopController.STOP_ENABLE_PATH, stopId)
                )
                .andReturn();
    }


}
