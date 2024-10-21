package com.moveSmart.busDataManager.route.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.EndPointInventory;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class RouteManagementIT extends EndPointInventory {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);


    //-----------------------------------------------------------------------------------------------------------------
    //CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a route creation request is received THEN returns route object and status 201 AND" +
            "WHEN same route creation request is received THEN returns status 409")

    void testRouteCreate() throws Exception {
        // First route creation request
        MvcResult newRoute = createRouteRequest(route);

        // Verify status and response content
        assertThat(HttpStatus.valueOf(newRoute.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Route responseBody = objectMapper.readValue(newRoute.getResponse().getContentAsString(), Route.class);
        checkRoutes(responseBody, route);

        // Verify route is saved on repository
        assertThat(routeRepository.findById(route.getId()).isPresent()).isTrue();
        checkRoutes(routeRepository.findById(route.getId()).get(), route);

        // Second route creation request (same route) should return conflict
        MvcResult routeConflict = createRouteRequest(route);

        // Verify status 409 (Conflict)
        assertThat(HttpStatus.valueOf(routeConflict.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);

        // Verifying the route still exists after conflict response
        assertThat(routeRepository.findById(route.getId()).isPresent()).isTrue();
        checkRoutes(routeRepository.findById(route.getId()).get(), route);
    }

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getSchedules()).isEqualTo(expected.getSchedules());
        assertThat(result.getStopId()).isEqualTo(expected.getStopId());
        //assertThat(result.getStops()).isEqualTo(expected.getStops());
    }
}
