package com.moveSmart.busDataManager.route.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.route.EndPointInventory;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
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

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    //-----------------------------------------------------------------------------------------------------------------
    // CREATE ENDPOINT
    @Test
    @DisplayName("WHEN a stop creation request is received THEN returns stop object and status 201 AND" +
            "WHEN same stop creation request is received THEN returns status 409")
    void testStopCreate() throws Exception {

        // First stop creation request
        MvcResult newStop = createStopRequest(stop);

        // Verify status 201 (Created) and stop returned
        assertThat(HttpStatus.valueOf(newStop.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        Stop responseBody = objectMapper.readValue(newStop.getResponse().getContentAsString(), Stop.class);
        checkStops(responseBody, stop);

        // Verify stop is saved on repository
        assertThat(stopRepository.findById(stop.getId()).isPresent()).isTrue();
        checkStops(stopRepository.findById(stop.getId()).get(), stop);

        // Second stop creation request (same stop) should return conflict
        MvcResult stopConflict = createStopRequest(stop);

        // Verify status 409 (Conflict)
        assertThat(HttpStatus.valueOf(stopConflict.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);

        // Verifying the stop still exists after conflict response
        assertThat(stopRepository.findById(stop.getId()).isPresent()).isTrue();
        checkStops(stopRepository.findById(stop.getId()).get(), stop);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // SUPPORT METHODS
    void checkStops(Stop result, Stop expected) {
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getName()).isEqualTo(expected.getName());
        assertThat(result.getLocation().latitude()).isEqualTo(expected.getLocation().latitude());
        assertThat(result.getLocation().longitude()).isEqualTo(expected.getLocation().longitude());
    }
}
