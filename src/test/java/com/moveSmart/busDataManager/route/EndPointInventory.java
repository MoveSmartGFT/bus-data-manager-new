package com.moveSmart.busDataManager.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.StopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ApplicationModuleTest
@AutoConfigureMockMvc
public abstract class EndPointInventory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected MvcResult createStopRequest(Stop stop) throws Exception {
        return this.mockMvc.perform(
                        post(StopController.STOP_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(stop))
                )
                .andReturn();
    }
}
