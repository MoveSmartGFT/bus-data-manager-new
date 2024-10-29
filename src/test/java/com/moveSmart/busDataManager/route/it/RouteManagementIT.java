package com.moveSmart.busDataManager.route.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.EndPointInventory;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteManagementIT extends EndPointInventory {

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Test
    void routeIT() throws Exception {
        StopRequest stopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);
        Stop stop = objectMapper.readValue(createStopRequest(stopRequest).getResponse().getContentAsString(), Stop.class);
        Route route = Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(stop.getId())));

        MvcResult createRouteResponse = createRouteRequest(route);
        assertThat(HttpStatus.valueOf(createRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Route createdRoute = objectMapper.readValue(createRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(createdRoute, route);

        MvcResult retrieveRouteResponse = getRouteRequest(route.getId());
        assertThat(HttpStatus.valueOf(retrieveRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRoute = objectMapper.readValue(retrieveRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRoute, route);

        MvcResult routeConflictResponse = createRouteRequest(route);
        assertThat(HttpStatus.valueOf(routeConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);
        MvcResult routeRetrievedPostConflictResponse = getRouteRequest(route.getId());
        assertThat(HttpStatus.valueOf(routeRetrievedPostConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRoutePostConflict = objectMapper.readValue(routeRetrievedPostConflictResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRoutePostConflict, route);

        Route notSavedRoute = Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(stop.getId())));

        MvcResult routeNotFoundResponse = getStopRequest(notSavedRoute.getId());
        assertThat(HttpStatus.valueOf(routeNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult retrieveStopIdsResponse = getStopIdsByRouteIdRequest(route.getId());
        assertThat(HttpStatus.valueOf(retrieveStopIdsResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        List<String> retrievedStopIds = objectMapper.readValue(retrieveStopIdsResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(retrievedStopIds, route.getStopIds());

        MvcResult stopIdsRouteDoesNotExistResponse = getStopIdsByRouteIdRequest(notSavedRoute.getId());
        assertThat(HttpStatus.valueOf(stopIdsRouteDoesNotExistResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        UpdateRouteRequest routeRequest = Instancio.create(RouteInstancioModels.getUpdateRouteRequestModelWithStops(List.of(stop.getId())));

        MvcResult updateRouteResponse = updateRouteRequest(route.getId(), routeRequest);
        assertThat(HttpStatus.valueOf(updateRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route updatedRoute = objectMapper.readValue(updateRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(updatedRoute, routeRequest.toRoute(route.getId()));
        MvcResult routeRetrievedUpdatedResponse = getRouteRequest(route.getId());
        assertThat(HttpStatus.valueOf(routeRetrievedUpdatedResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteUpdated = objectMapper.readValue(routeRetrievedUpdatedResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteUpdated, routeRequest.toRoute(route.getId()));

        MvcResult updatedRouteNotFoundResponse = updateRouteRequest("Route1", routeRequest);
        assertThat(HttpStatus.valueOf(updatedRouteNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
        assertThat(result.getSchedules()).isEqualTo(expected.getSchedules());
    }
}