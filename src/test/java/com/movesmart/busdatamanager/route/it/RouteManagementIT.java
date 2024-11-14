package com.movesmart.busdatamanager.route.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.CreateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteStopsRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
import jakarta.transaction.Transactional;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class RouteManagementIT extends EndPointRouteInventory {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void cleanDatabase() {
        mongoTemplate.getDb().drop();
    }

    @Transactional
    @Test
    void routeIT() throws Exception {
        StopRequest stopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);
        Stop stop = objectMapper.readValue(createStopRequest(stopRequest).getResponse().getContentAsString(), Stop.class);
        CreateRouteRequest firstRoute = Instancio.create(RouteInstancioModels.getCreateRouteRequestModelWithStops(List.of(stop.getId())));
        CreateRouteRequest secondRoute = Instancio.create(RouteInstancioModels.getCreateRouteRequestModelWithStops(List.of(stop.getId())));

        MvcResult createRouteResponse = createRouteRequest(firstRoute);
        assertThat(HttpStatus.valueOf(createRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Route createdRoute = objectMapper.readValue(createRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(createdRoute, firstRoute);

        MvcResult retrieveRouteResponse = getRouteRequest(firstRoute.id());
        assertThat(HttpStatus.valueOf(retrieveRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRoute = objectMapper.readValue(retrieveRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRoute, firstRoute);

        MvcResult routeConflictResponse = createRouteRequest(firstRoute);
        assertThat(HttpStatus.valueOf(routeConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);
        MvcResult routeRetrievedPostConflictResponse = getRouteRequest(firstRoute.id());
        assertThat(HttpStatus.valueOf(routeRetrievedPostConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRoutePostConflict = objectMapper.readValue(routeRetrievedPostConflictResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRoutePostConflict, firstRoute);

        MvcResult createSecondRouteResponse = createRouteRequest(secondRoute);
        assertThat(HttpStatus.valueOf(createSecondRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);

        MvcResult retrieveAllRoutesResponse = getAllRoutesRequest();
        assertThat(HttpStatus.valueOf(retrieveAllRoutesResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        List<Route> retrievedAllRoutes = objectMapper.readValue(retrieveAllRoutesResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(retrievedAllRoutes).hasSize(2);
        checkRoutes(retrievedAllRoutes.get(0), firstRoute);
        checkRoutes(retrievedAllRoutes.get(1), secondRoute);

        CreateRouteRequest notSavedRoute = Instancio.create(RouteInstancioModels.getCreateRouteRequestModelWithStops(List.of(stop.getId())));

        MvcResult routeNotFoundResponse = getStopRequest(notSavedRoute.id());
        assertThat(HttpStatus.valueOf(routeNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult retrieveStopIdsResponse = getStopIdsByRouteIdRequest(firstRoute.id());
        assertThat(HttpStatus.valueOf(retrieveStopIdsResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        List<String> retrievedStopIds = objectMapper.readValue(retrieveStopIdsResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(retrievedStopIds, firstRoute.stopIds());

        MvcResult stopIdsRouteDoesNotExistResponse = getStopIdsByRouteIdRequest(notSavedRoute.id());
        assertThat(HttpStatus.valueOf(stopIdsRouteDoesNotExistResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        UpdateRouteRequest routeRequest = Instancio.create(RouteInstancioModels.getUpdateRouteRequestModelWithStops(List.of(stop.getId())));

        MvcResult updateRouteResponse = updateRouteRequest(firstRoute.id(), routeRequest);
        assertThat(HttpStatus.valueOf(updateRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route updatedRoute = objectMapper.readValue(updateRouteResponse.getResponse().getContentAsString(), Route.class);
        Route firstRouteUpdated = routeRequest.toRoute(firstRoute.id());
        checkRoutes(updatedRoute, firstRouteUpdated);
        MvcResult routeRetrievedUpdatedResponse = getRouteRequest(firstRouteUpdated.getId());
        assertThat(HttpStatus.valueOf(routeRetrievedUpdatedResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteUpdated = objectMapper.readValue(routeRetrievedUpdatedResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteUpdated, firstRouteUpdated);

        MvcResult updatedRouteNotFoundResponse = updateRouteRequest("Route1", routeRequest);
        assertThat(HttpStatus.valueOf(updatedRouteNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        UpdateRouteStopsRequest routeRequestStops = Instancio.create(RouteInstancioModels.getUpdateRouteStopsRequestModelWithStops(List.of(stop.getId())));

        MvcResult updateRouteStopsResponse = updateRouteStopsRequest(firstRouteUpdated.getId(), routeRequestStops);
        assertThat(HttpStatus.valueOf(updateRouteStopsResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        firstRouteUpdated = objectMapper.readValue(updateRouteStopsResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(firstRouteUpdated, routeRequestStops.toRoute(firstRouteUpdated));
        MvcResult routeRetrievedUpdatedListResponse = getRouteRequest(firstRouteUpdated.getId());
        assertThat(HttpStatus.valueOf(routeRetrievedUpdatedListResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        assertThat(HttpStatus.valueOf(routeRetrievedUpdatedListResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteStopsUpdated = objectMapper.readValue(routeRetrievedUpdatedResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteStopsUpdated, firstRouteUpdated);

        MvcResult updatedRouteStopNotFoundResponse = updateRouteStopsRequest("NonExistingStop", routeRequestStops);
        assertThat(HttpStatus.valueOf(updatedRouteStopNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disabledRouteResponse = disableRouteRequest(firstRouteUpdated.getId());
        assertThat(HttpStatus.valueOf(disabledRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteDisabled = objectMapper.readValue(disabledRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteDisabled, firstRouteUpdated);
        MvcResult getDisabledResponse = getRouteRequest(firstRouteUpdated.getId());
        Route retrievedGetRouteDisabled = objectMapper.readValue(getDisabledResponse.getResponse().getContentAsString(), Route.class);
        assertThat(retrievedGetRouteDisabled.getStatus()).isEqualTo(Route.Status.Disabled);

        MvcResult disabledRouteNotFoundResponse = disableRouteRequest("Route1");
        assertThat(HttpStatus.valueOf(disabledRouteNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disabledRouteAlreadyDisabledResponse = disableRouteRequest(firstRouteUpdated.getId());
        assertThat(HttpStatus.valueOf(disabledRouteAlreadyDisabledResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enabledRouteResponse = enableRouteRequest(firstRouteUpdated.getId());
        assertThat(HttpStatus.valueOf(enabledRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteEnabled = objectMapper.readValue(enabledRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteEnabled, firstRouteUpdated);
        MvcResult getEnabledResponse = getRouteRequest(firstRouteUpdated.getId());
        Route retrievedGetRouteEnabled = objectMapper.readValue(getEnabledResponse.getResponse().getContentAsString(), Route.class);
        assertThat(retrievedGetRouteEnabled.getStatus()).isEqualTo(Route.Status.Enabled);

        MvcResult enabledRouteNotFoundResponse = enableRouteRequest("Route1");
        assertThat(HttpStatus.valueOf(enabledRouteNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enabledRouteAlreadyDisabledResponse = enableRouteRequest(firstRoute.id());
        assertThat(HttpStatus.valueOf(enabledRouteAlreadyDisabledResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult deleteRouteResponse = deleteRouteRequest(secondRoute.id());
        assertThat(HttpStatus.valueOf(deleteRouteResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Route retrievedRouteDeleted = objectMapper.readValue(deleteRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteDeleted, secondRoute);
        MvcResult getDeletedResponse = getRouteRequest(secondRoute.id());
        assertThat(HttpStatus.valueOf(getDeletedResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkRoutes(Route result, CreateRouteRequest expected) {
        assertThat(result.getId()).isEqualTo(expected.id());
        assertThat(result.getName()).isEqualTo(expected.name());
        assertThat(result.getStopIds()).isEqualTo(expected.stopIds());
        assertThat(result.getSchedules())
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(t -> t.truncatedTo(ChronoUnit.MILLIS)),
                        LocalDateTime.class)
                .isEqualTo(expected.schedules());
    }

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
        assertThat(result.getSchedules())
                .usingRecursiveComparison()
                .withComparatorForType(
                        Comparator.comparing(t -> t.truncatedTo(ChronoUnit.MILLIS)),
                        LocalDateTime.class)
                .isEqualTo(expected.getSchedules());
    }
}