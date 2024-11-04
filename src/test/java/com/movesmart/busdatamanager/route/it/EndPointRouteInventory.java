package com.movesmart.busdatamanager.route.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.infrastructure.api.route.RouteController;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.StopController;
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
public abstract class EndPointRouteInventory extends EndPointStopInventory {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    protected MvcResult createRouteRequest(Route route) throws Exception {
        return this.mockMvc.perform(
                        post(RouteController.ROUTE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(route))
                )
                .andReturn();
    }

    protected MvcResult getRouteRequest(String routeId) throws Exception {
        return this.mockMvc.perform(
                        get(RouteController.ROUTE_PATH+StopController.STOP_ID_PATH, routeId)
                )
                .andReturn();
    }

    protected MvcResult getAllRoutesRequest() throws Exception {
        return this.mockMvc.perform(
                        get(RouteController.ROUTE_PATH)
                )
                .andReturn();
    }

    protected MvcResult getStopIdsByRouteIdRequest(String routeId) throws Exception {
        return this.mockMvc.perform(
                        get(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH+RouteController.STOPS_PATH, routeId)
                )
                .andReturn();
    }

    protected MvcResult updateRouteRequest(String routeId, UpdateRouteRequest route) throws Exception {
        return this.mockMvc.perform(
                        put(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, routeId)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(route))
                )
                .andReturn();
    }
}
