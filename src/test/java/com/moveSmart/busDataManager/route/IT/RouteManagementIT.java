package com.moveSmart.busDataManager.route.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.EndPointInventory;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
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
    private StopRepository stopRepository;

    @Autowired
    private ObjectMapper objectMapper;

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a route creation request is received THEN returns route object and status 201 AND" +
            "WHEN same route creation request is received THEN returns status 409")
    void testRouteCreate() throws Exception {

        List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);
        stops.forEach(stop -> stopRepository.save(stop));
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

        MvcResult newRoute = createRouteRequest(route);
        assertThat(HttpStatus.valueOf(newRoute.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);

        Route responseBody = objectMapper.readValue(newRoute.getResponse().getContentAsString(), Route.class);
        checkRoutes(responseBody, route);

        assertThat(routeRepository.findById(route.getId()).isPresent()).isTrue();
        checkRoutes(routeRepository.findById(route.getId()).get(), route);

        MvcResult routeConflict = createRouteRequest(route);

        assertThat(HttpStatus.valueOf(routeConflict.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);
        assertThat(routeRepository.findById(route.getId()).isPresent()).isTrue();
        checkRoutes(routeRepository.findById(route.getId()).get(), route);
    }

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getSchedules()).isEqualTo(expected.getSchedules());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //GET STOP IDS ENDPOINT

    @Test
    @DisplayName("WHEN a Route retrieval request is received AND said Route exists THEN return the Route and status 200")
    void getRoute() throws Exception {
        List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);
        stops.forEach(stop -> stopRepository.save(stop));
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

        createRouteRequest(route);
        MvcResult routeRetrieved = getRouteRequest(route.getId());

        assertThat(HttpStatus.valueOf(routeRetrieved.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route responseBody = objectMapper.readValue(routeRetrieved.getResponse().getContentAsString(), Route.class);
        checkRoutes(responseBody, route);
    }

    @Test
    @DisplayName("WHEN a Stop retrieval request is received AND said Stop does not exist THEN returns status 404")
    void getRouteDoesNotExist() throws Exception {
        List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

        MvcResult routeNotFound = getStopRequest(route.getId());
        assertThat(HttpStatus.valueOf(routeNotFound.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("WHEN a Stop Id list retrieval request is received THEN returns the list of Ids of the Stops belonging to the Route and status 200")
    void getStopIds() throws Exception {

        List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);
        stops.forEach(stop -> stopRepository.save(stop));
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

        createRouteRequest(route);
        MvcResult stopIds = getStopIdsRequest(route.getId());
        assertThat(HttpStatus.valueOf(stopIds.getResponse().getStatus())).isEqualTo(HttpStatus.OK);

        List<String> responseBody = objectMapper.readValue(stopIds.getResponse().getContentAsString(), List.class);
        checkStopIds(responseBody, route.getStopIds());
    }

    void checkStopIds(List<String> result, List<String> expected) {
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("WHEN a Stop Id list retrieval request is received AND the Route does not exist THEN return status 404")

    void getStopIdsRouteNotFound() throws Exception {
        List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);
        final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

        // Stop Id List retrieval request
        MvcResult stopIds = getStopIdsRequest(route.getId());

        // Verify status 404 (Not found)
        assertThat(HttpStatus.valueOf(stopIds.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }
}