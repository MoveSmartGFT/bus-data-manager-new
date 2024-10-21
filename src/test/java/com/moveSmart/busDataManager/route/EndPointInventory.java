package com.moveSmart.busDataManager.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.infrastructure.api.route.RouteController;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.StopController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    protected MvcResult createRouteRequest(Route route) throws Exception {
        return this.mockMvc.perform(
                        post(RouteController.ROUTE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(route))
                )
                .andReturn();
    }

    protected MvcResult getStopRequest(String stopId) throws Exception {
        return this.mockMvc.perform(
                        get(StopController.STOP_PATH+StopController.STOP_ID_PATH, stopId)
                )
                .andReturn();
    }

    protected MvcResult getStopIdsRequest(String routeId) throws Exception {
        return this.mockMvc.perform(
                        get(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH+RouteController.STOPS_PATH, routeId)
                )
                .andReturn();
    }


}
