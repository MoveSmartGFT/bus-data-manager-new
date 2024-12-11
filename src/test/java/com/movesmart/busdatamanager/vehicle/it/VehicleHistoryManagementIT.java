package com.movesmart.busdatamanager.vehicle.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryResponse;
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

@SpringBootTest
@ActiveProfiles("test")
public class VehicleHistoryManagementIT extends EndPointVehicleHistoryInventory {
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
    void vehicleHistoryIT() throws Exception {

        VehicleHistoryRequest firstVehicleHistoryRequest =
                Instancio.create(VehicleInstancioModels.VEHICLE_HISTORY_REQUEST_MODEL);
        VehicleHistoryRequest secondVehicleHistoryRequest =
                Instancio.create(VehicleInstancioModels.VEHICLE_HISTORY_REQUEST_MODEL);

        MvcResult createVehicleHistoryResponse = createVehicleHistoryRequest(firstVehicleHistoryRequest);
        assertThat(HttpStatus.valueOf(createVehicleHistoryResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        VehicleHistoryResponse createdVehicleHistory = objectMapper.readValue(
                createVehicleHistoryResponse.getResponse().getContentAsString(), VehicleHistoryResponse.class);
        checkVehicleHistories(createdVehicleHistory, firstVehicleHistoryRequest);

        MvcResult createSecondVehicleResponse = createVehicleHistoryRequest(secondVehicleHistoryRequest);
        assertThat(HttpStatus.valueOf(createSecondVehicleResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
    }

    void checkVehicleHistories(VehicleHistoryResponse result, VehicleHistoryRequest expected) {
        assertThat(result.routeId()).isEqualTo(expected.routeId());
        assertThat(result.driverId()).isEqualTo(expected.driverId());
        assertThat(result.startTime()).isEqualTo(expected.startTime());
    }
}
