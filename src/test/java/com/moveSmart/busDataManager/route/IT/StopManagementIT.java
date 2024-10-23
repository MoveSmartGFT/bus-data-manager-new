package com.moveSmart.busDataManager.route.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.EndPointInventory;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.UpdateStopRequest;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;

public class StopManagementIT extends EndPointInventory {

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private ObjectMapper objectMapper;

    //-----------------------------------------------------------------------------------------------------------------
    // CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a stop creation request is received THEN returns stop object and status 201 AND" +
            "WHEN same stop creation request is received THEN returns status 409")
    void testStopCreate() throws Exception {
        final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        // First stop creation request
        MvcResult newStop = createStopRequest(stop);

        // Verify status 201 (Created) and stop returned
        assertThat(HttpStatus.valueOf(newStop.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Stop responseBody = objectMapper.readValue(newStop.getResponse().getContentAsString(), Stop.class);
        checkStop(responseBody, stop);

        // Verify stop is saved on repository
        assertThat(stopRepository.findById(stop.getId()).isPresent()).isTrue();
        checkStop(stopRepository.findById(stop.getId()).get(), stop);

        // Second stop creation request (same stop) should return conflict
        MvcResult stopConflict = createStopRequest(stop);

        // Verify status 409 (Conflict)
        assertThat(HttpStatus.valueOf(stopConflict.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);

        // Verifying the stop still exists after conflict response
        assertThat(stopRepository.findById(stop.getId()).isPresent()).isTrue();
        checkStop(stopRepository.findById(stop.getId()).get(), stop);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // GET ENDPOINT

    @Test
    @DisplayName("WHEN a Stop retrieval request is received AND said Stop exists THEN return the Stop and status 200")
    void testGetStop() throws Exception {
        final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        // Stop creation request
        createStopRequest(stop);

        // Stop retrieval request. Should return stop
        MvcResult stopRetrieved = getStopRequest(stop.getId());

        // Verify status 200 (OK)
        assertThat(HttpStatus.valueOf(stopRetrieved.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop responseBody = objectMapper.readValue(stopRetrieved.getResponse().getContentAsString(), Stop.class);
        checkStop(responseBody, stop);
    }

    @Test
    @DisplayName("WHEN a Stop retrieval request is received AND said Stop does not exist THEN returns status 404")
    void testGetStopDoesNotExist() throws Exception {
        final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        // Stop retrieval request
        MvcResult stopNotFound = getStopRequest(stop.getId());

        // Verify status 404 (Not found)
        assertThat(HttpStatus.valueOf(stopNotFound.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // UPDATE ENDPOINT

    @Test
    @DisplayName("WHEN a Stop update request is received AND Stop exists THEN return the Stop and status 200")
    void testUpdateStop() throws Exception {
        final UpdateStopRequest stopRequest = Instancio.create(RouteInstancioModels.UPDATE_STOP_REQUEST_MODEL);
        final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

        // Stop creation request
        createStopRequest(stop);

        // Stop update request
        MvcResult updatedStop = updateStopRequest(stop.getId(), stopRequest);

        // Verify status 200 (OK)
        assertThat(HttpStatus.valueOf(updatedStop.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Stop responseBody = objectMapper.readValue(updatedStop.getResponse().getContentAsString(), Stop.class);
        checkStop(responseBody, responseBody);
    }

    @Test
    @DisplayName("WHEN a Stop update request is received AND Stop does not exist THEN returns status 404")
    void testUpdateStopDoesNotExist() throws Exception {
        final UpdateStopRequest stopRequest = Instancio.create(RouteInstancioModels.UPDATE_STOP_REQUEST_MODEL);

        // Stop update request
        MvcResult updatedStop = updateStopRequest("Stop1", stopRequest);

        // Verify status 404 (Not found)
        assertThat(HttpStatus.valueOf(updatedStop.getResponse().getStatus())).isEqualTo(HttpStatus.NOT_FOUND);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // SUPPORT METHODS

    void checkStop(Stop result, Stop expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getLocation().latitude()).isEqualTo(expected.getLocation().latitude());
        assertThat(result.getLocation().longitude()).isEqualTo(expected.getLocation().longitude());
    }
}
