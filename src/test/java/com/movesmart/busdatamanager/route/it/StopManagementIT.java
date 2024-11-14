package com.movesmart.busdatamanager.route.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
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
public class StopManagementIT extends EndPointStopInventory {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @AfterEach
    void cleanDatabase() {
        mongoTemplate.getDb().drop();
    }

    @Test
    void stopIT() throws Exception {
        StopRequest firstStopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);
        StopRequest secondStopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        MvcResult createStopResponse = createStopRequest(firstStopRequest);
        assertThat(HttpStatus.valueOf(createStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        Stop createdStop =
                objectMapper.readValue(createStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(createdStop, firstStopRequest.toStop());

        MvcResult retrieveStopResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(retrieveStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedStop =
                objectMapper.readValue(retrieveStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStop, firstStopRequest.toStop());

        MvcResult createSecondStopResponse = createStopRequest(secondStopRequest);
        assertThat(HttpStatus.valueOf(createSecondStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);

        MvcResult retrieveAllStopsResponse = getAllStopsRequest();
        assertThat(HttpStatus.valueOf(retrieveAllStopsResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        List<Stop> retrievedAllStops = objectMapper.readValue(
                retrieveAllStopsResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(retrievedAllStops).hasSize(2);
        checkStop(retrievedAllStops.get(0), firstStopRequest.toStop());
        checkStop(retrievedAllStops.get(1), secondStopRequest.toStop());

        Stop notSavedStop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        MvcResult stopNotFoundResponse = getStopRequest(notSavedStop.getId());
        assertThat(HttpStatus.valueOf(stopNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        StopRequest stopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        MvcResult updateStopResponse = updateStopRequest(createdStop.getId(), stopRequest);
        assertThat(HttpStatus.valueOf(updateStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop updatedStop =
                objectMapper.readValue(updateStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(updatedStop, stopRequest.toStop());
        MvcResult stopRetrievedUpdatedResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(stopRetrievedUpdatedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedStopUpdated = objectMapper.readValue(
                stopRetrievedUpdatedResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStopUpdated, stopRequest.toStop());

        MvcResult updateStopNotFoundResponse = updateStopRequest("Stop1", firstStopRequest);
        assertThat(HttpStatus.valueOf(updateStopNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disableStopResponse = disableStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(disableStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedDisabledStop =
                objectMapper.readValue(disableStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedDisabledStop, stopRequest.toStop(createdStop.getId()));
        MvcResult getDisabledResponse = getStopRequest(createdStop.getId());
        Stop retrievedGetStopDisabled =
                objectMapper.readValue(getDisabledResponse.getResponse().getContentAsString(), Stop.class);
        assertThat(retrievedGetStopDisabled.getStatus()).isEqualTo(Stop.Status.Disabled);

        MvcResult disabledStopNotFoundResponse = disableStopRequest("Stop1");
        assertThat(HttpStatus.valueOf(
                disabledStopNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult disableStopAlreadyDisabledResponse = disableStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(disableStopAlreadyDisabledResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enableStopResponse = enableStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(enableStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedEnabledStop =
                objectMapper.readValue(enableStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedEnabledStop, stopRequest.toStop(createdStop.getId()));
        MvcResult getEnabledResponse = getStopRequest(createdStop.getId());
        Stop retrievedGetEnabledStop =
                objectMapper.readValue(getEnabledResponse.getResponse().getContentAsString(), Stop.class);
        assertThat(retrievedGetEnabledStop.getStatus()).isEqualTo(Stop.Status.Enabled);

        MvcResult enableStopNotFoundResponse = enableStopRequest("NonExistingStopId");
        assertThat(HttpStatus.valueOf(enableStopNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult enableStopAlreadyEnabledResponse = enableStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(enableStopAlreadyEnabledResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult stopConflictResponse = createStopRequest(firstStopRequest);
        assertThat(HttpStatus.valueOf(stopConflictResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CONFLICT);
        MvcResult stopRetrievedPostConflictResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(
                        stopRetrievedPostConflictResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedStopPostConflict =
                objectMapper.readValue(retrieveStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStopPostConflict, firstStopRequest.toStop());








        MvcResult removeStopIdFromRoutesResponse = removeStopIdFromRoutesRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(
                        removeStopIdFromRoutesResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        String removedStopIdFromRoutes =
                removeStopIdFromRoutesResponse.getResponse().getContentAsString();
        assertThat(removedStopIdFromRoutes)
                .isEqualTo("Stop with id %s removed from %s routes".formatted(createdStop.getId(), 0));

        MvcResult deleteStopResponse =
                deleteStopRequest(secondStopRequest.toStop().getId());
        assertThat(HttpStatus.valueOf(deleteStopResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Stop retrievedStopDeleted =
                objectMapper.readValue(deleteStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStopDeleted, secondStopRequest.toStop());
        MvcResult getDeletedResponse = getStopRequest(secondStopRequest.toStop().getId());
        assertThat(HttpStatus.valueOf(getDeletedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkStop(Stop result, Stop expected) {
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getLocation().latitude())
                .isEqualTo(expected.getLocation().latitude());
        assertThat(result.getLocation().longitude())
                .isEqualTo(expected.getLocation().longitude());
    }
}
