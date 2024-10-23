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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteManagementIT extends EndPointInventory {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a route creation request is received THEN returns route object and status 201 AND" +
            "WHEN same route creation request is received THEN returns status 409")

    void testRouteCreate() throws Exception {
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);


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

    //-----------------------------------------------------------------------------------------------------------------
    //GET STOP IDS ENDPOINT

    @Test
    @DisplayName("WHEN a Stop Id list retrieval request is received THEN returns the list of Ids of the Stops belonging to the Route and status 200")
    void testGetStopIds() throws Exception {
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

        // Route creation request
        createRouteRequest(route);

        // Stop Id List retrieval request
        MvcResult stopIds = getStopIdsByRouteIdRequest(route.getId());

        // Verify status and response content
        assertThat(HttpStatus.valueOf(stopIds.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        List<String> responseBody = objectMapper.readValue(stopIds.getResponse().getContentAsString(), List.class);

        assertEquals(responseBody, route.getStopIds());
    }

    @Test
    @DisplayName("WHEN a Stop Id list retrieval request is received AND the Route does not exist THEN return status 404")

    void testGetStopIdsRouteNotFound() throws Exception {
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

        // Stop Id List retrieval request
        MvcResult stopIds = getStopIdsByRouteIdRequest(route.getId());

        // Verify status 404 (Not found)
        assertThat(HttpStatus.valueOf(stopIds.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // SUPPORT METHODS

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getSchedules()).isEqualTo(expected.getSchedules());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
    }
}
