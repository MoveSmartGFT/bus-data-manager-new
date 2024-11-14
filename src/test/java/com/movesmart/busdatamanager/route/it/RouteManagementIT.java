package com.movesmart.busdatamanager.route.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.CreateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.UpdateRouteStopsRequest;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

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
        Stop stop = objectMapper.readValue(
                createStopRequest(stopRequest).getResponse().getContentAsString(), Stop.class);
        CreateRouteRequest firstCreateRouteRequest =
                Instancio.create(RouteInstancioModels.getCreateRouteRequestModelWithStops(List.of(stop.getId())));
        CreateRouteRequest secondCreateRouteRequest =
                Instancio.create(RouteInstancioModels.getCreateRouteRequestModelWithStops(List.of(stop.getId())));
        Route firstRoute =
                Instancio.create(RouteInstancioModels.getRouteModelFromCreateRequest(firstCreateRouteRequest));

        MvcResult createRouteResponse = createRouteRequest(firstCreateRouteRequest);
        assertThat(HttpStatus.valueOf(createRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        Route firstCreatedRoute =
                objectMapper.readValue(createRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRouteCreated(firstCreatedRoute, firstRoute);

        MvcResult retrieveRouteResponse = getRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(retrieveRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRoute =
                objectMapper.readValue(retrieveRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRoute, firstCreatedRoute);

        MvcResult createSecondRouteResponse = createRouteRequest(secondCreateRouteRequest);
        assertThat(HttpStatus.valueOf(createSecondRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        Route secondCreatedRoute =
                objectMapper.readValue(createSecondRouteResponse.getResponse().getContentAsString(), Route.class);

        MvcResult retrieveAllRoutesResponse = getAllRoutesRequest();
        assertThat(HttpStatus.valueOf(retrieveAllRoutesResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        List<Route> retrievedAllRoutes = objectMapper.readValue(
                retrieveAllRoutesResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(retrievedAllRoutes).hasSize(2);
        checkRoutes(retrievedAllRoutes.get(0), firstCreatedRoute);
        checkRoutes(retrievedAllRoutes.get(1), secondCreatedRoute);

        Route notSavedRoute = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

        MvcResult routeNotFoundResponse = getStopRequest(notSavedRoute.getId());
        assertThat(HttpStatus.valueOf(routeNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult retrieveStopIdsResponse = getStopIdsByRouteIdRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(retrieveStopIdsResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        List<String> retrievedStopIds = objectMapper.readValue(
                retrieveStopIdsResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertEquals(retrievedStopIds, firstCreatedRoute.getStopIds());

        MvcResult stopIdsRouteDoesNotExistResponse = getStopIdsByRouteIdRequest(notSavedRoute.getId());
        assertThat(HttpStatus.valueOf(
                        stopIdsRouteDoesNotExistResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        UpdateRouteRequest routeRequest =
                Instancio.create(RouteInstancioModels.getUpdateRouteRequestModelWithStops(List.of(stop.getId())));

        MvcResult updateRouteResponse = updateRouteRequest(firstCreatedRoute.getId(), routeRequest);
        assertThat(HttpStatus.valueOf(updateRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route updatedRoute =
                objectMapper.readValue(updateRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(updatedRoute, routeRequest.toRoute(firstCreatedRoute.getId()));
        MvcResult routeRetrievedUpdatedResponse = getRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(
                        routeRetrievedUpdatedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRouteUpdated = objectMapper.readValue(
                routeRetrievedUpdatedResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteUpdated, routeRequest.toRoute(firstCreatedRoute.getId()));

        MvcResult updatedRouteNotFoundResponse = updateRouteRequest("Route1", routeRequest);
        assertThat(HttpStatus.valueOf(updatedRouteNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        UpdateRouteStopsRequest routeRequestStops =
                Instancio.create(RouteInstancioModels.getUpdateRouteStopsRequestModelWithStops(List.of(stop.getId())));

        MvcResult updateRouteStopsResponse = updateRouteStopsRequest(firstCreatedRoute.getId(), routeRequestStops);
        assertThat(HttpStatus.valueOf(updateRouteStopsResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route updatedRouteStops =
                objectMapper.readValue(updateRouteStopsResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(updatedRouteStops, routeRequestStops.toRoute(updatedRouteStops));
        MvcResult routeRetrievedUpdatedListResponse = getRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(
                        routeRetrievedUpdatedListResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        assertThat(HttpStatus.valueOf(
                        routeRetrievedUpdatedListResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRouteStopsUpdated = objectMapper.readValue(
                routeRetrievedUpdatedResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteStopsUpdated, routeRequest.toRoute(firstCreatedRoute.getId()));

        MvcResult updatedRouteStopNotFoundResponse = updateRouteStopsRequest("NonExistingStop", routeRequestStops);
        assertThat(HttpStatus.valueOf(
                        updatedRouteStopNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disabledRouteResponse = disableRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(disabledRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRouteDisabled =
                objectMapper.readValue(disabledRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteDisabled, routeRequest.toRoute(firstCreatedRoute.getId()));
        checkRoutes(retrievedRouteDisabled, routeRequest.toRoute(firstCreatedRoute.getId()));
        MvcResult getDisabledResponse = getRouteRequest(firstCreatedRoute.getId());
        Route retrievedGetRouteDisabled =
                objectMapper.readValue(getDisabledResponse.getResponse().getContentAsString(), Route.class);
        assertThat(retrievedGetRouteDisabled.getStatus()).isEqualTo(Route.Status.Disabled);

        MvcResult disabledRouteNotFoundResponse = disableRouteRequest("Route1");
        assertThat(HttpStatus.valueOf(
                        disabledRouteNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disabledRouteAlreadyDisabledResponse = disableRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(
                        disabledRouteAlreadyDisabledResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enabledRouteResponse = enableRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(enabledRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRouteEnabled =
                objectMapper.readValue(enabledRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteEnabled, routeRequest.toRoute(firstCreatedRoute.getId()));
        MvcResult getEnabledResponse = getRouteRequest(firstCreatedRoute.getId());
        Route retrievedGetRouteEnabled =
                objectMapper.readValue(getEnabledResponse.getResponse().getContentAsString(), Route.class);
        assertThat(retrievedGetRouteEnabled.getStatus()).isEqualTo(Route.Status.Enabled);

        MvcResult enabledRouteNotFoundResponse = enableRouteRequest("Route1");
        assertThat(HttpStatus.valueOf(enabledRouteNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enabledRouteAlreadyDisabledResponse = enableRouteRequest(firstCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(
                        enabledRouteAlreadyDisabledResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult deleteRouteResponse = deleteRouteRequest(secondCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(deleteRouteResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Route retrievedRouteDeleted =
                objectMapper.readValue(deleteRouteResponse.getResponse().getContentAsString(), Route.class);
        checkRoutes(retrievedRouteDeleted, secondCreatedRoute);
        MvcResult getDeletedResponse = getRouteRequest(secondCreatedRoute.getId());
        assertThat(HttpStatus.valueOf(getDeletedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkRouteCreated(Route result, Route expected) {
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
        assertThat(result.getSchedules())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing(t -> t.truncatedTo(ChronoUnit.MILLIS)), LocalDateTime.class)
                .isEqualTo(expected.getSchedules());
    }

    void checkRoutes(Route result, Route expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getStopIds()).isEqualTo(expected.getStopIds());
        assertThat(result.getSchedules())
                .usingRecursiveComparison()
                .withComparatorForType(Comparator.comparing(t -> t.truncatedTo(ChronoUnit.MILLIS)), LocalDateTime.class)
                .isEqualTo(expected.getSchedules());
    }
}
