package com.movesmart.busdatamanager.route.it;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StopManagementIT extends EndPointStopInventory {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void stopIT() throws Exception {
        StopRequest firstStopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);
        StopRequest secondStopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        MvcResult createStopResponse = createStopRequest(firstStopRequest);
        assertThat(HttpStatus.valueOf(createStopResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Stop createdStop = objectMapper.readValue(createStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(createdStop, firstStopRequest);

        MvcResult retrieveStopResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(retrieveStopResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop retrievedStop = objectMapper.readValue(retrieveStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStop, firstStopRequest);

        MvcResult stopConflictResponse = createStopRequest(firstStopRequest);
        assertThat(HttpStatus.valueOf(stopConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);
        MvcResult stopRetrievedPostConflictResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(stopRetrievedPostConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop retrievedStopPostConflict = objectMapper.readValue(retrieveStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStopPostConflict, firstStopRequest);

        MvcResult createSecondStopResponse = createStopRequest(secondStopRequest);
        assertThat(HttpStatus.valueOf(createSecondStopResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);

        MvcResult retrieveAllStopsResponse = getAllStopsRequest();
        assertThat(HttpStatus.valueOf(retrieveAllStopsResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        List<Stop> retrievedAllStops = objectMapper.readValue(retrieveAllStopsResponse.getResponse().getContentAsString(), new TypeReference<>() {});
        assertThat(retrievedAllStops).hasSize(2);
        checkStop(retrievedAllStops.get(0), firstStopRequest);
        checkStop(retrievedAllStops.get(1), secondStopRequest);

        Stop notSavedStop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        MvcResult stopNotFoundResponse = getStopRequest(notSavedStop.getId());
        assertThat(HttpStatus.valueOf(stopNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);

        StopRequest updateStopRequest = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        MvcResult updateStopResponse = updateStopRequest(createdStop.getId(), updateStopRequest);
        assertThat(HttpStatus.valueOf(updateStopResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop updatedStop = objectMapper.readValue(updateStopResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(updatedStop, updateStopRequest);
        MvcResult stopRetrievedUpdatedResponse = getStopRequest(createdStop.getId());
        assertThat(HttpStatus.valueOf(stopRetrievedUpdatedResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop retrievedStopUpdated = objectMapper.readValue(stopRetrievedUpdatedResponse.getResponse().getContentAsString(), Stop.class);
        checkStop(retrievedStopUpdated, updateStopRequest);

        MvcResult updateStopNotFoundResponse = updateStopRequest("Stop1", firstStopRequest);
        assertThat(HttpStatus.valueOf(updateStopNotFoundResponse.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkStop(Stop result, StopRequest expected) {
        assertThat(result.getName()).isEqualTo(expected.name());
        assertThat(result.getLocation().latitude()).isEqualTo(expected.location().latitude());
        assertThat(result.getLocation().longitude()).isEqualTo(expected.location().longitude());
    }
}