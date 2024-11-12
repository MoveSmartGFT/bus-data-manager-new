package com.movesmart.busdatamanager.vehicle.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class VehicleManagementIT extends EndPointVehicleInventory {

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
    void vehicleIT() throws Exception {
        VehicleRequest firstVehicleRequest = Instancio.create(VehicleInstancioModels.VEHICLE_REQUEST_MODEL);

        MvcResult createVehicleResponse = createVehicleRequest(firstVehicleRequest);
        assertThat(HttpStatus.valueOf(createVehicleResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CREATED);
        VehicleResponse createdVehicle = objectMapper.readValue(createVehicleResponse.getResponse().getContentAsString(),
                VehicleResponse.class);
        checkVehicles(createdVehicle, firstVehicleRequest);

        MvcResult retrieveVehicleResponse = getVehicleRequest(firstVehicleRequest.plateNumber());
        assertThat(HttpStatus.valueOf(retrieveVehicleResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehicle = objectMapper.readValue(retrieveVehicleResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehicle, firstVehicleRequest);

        MvcResult VehicleConflictResponse = createVehicleRequest(firstVehicleRequest);
        assertThat(HttpStatus.valueOf(VehicleConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.CONFLICT);
        MvcResult retrieveVehiclePostConflictResponse = getVehicleRequest(firstVehicleRequest.plateNumber());
        assertThat(HttpStatus.valueOf(retrieveVehiclePostConflictResponse.getResponse().getStatus())).isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehiclePostConflict = objectMapper.readValue(retrieveVehiclePostConflictResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehiclePostConflict, firstVehicleRequest);
    }

    void checkVehicles(Vehicle result, VehicleRequest expected) {
        assertThat(result.getPlateNumber()).isEqualTo(expected.plateNumber());
        assertThat(result.getCapacity()).isEqualTo(expected.capacity());
        assertThat(result.getStatus()).isEqualTo(expected.status());
        assertThat(result.getType()).isEqualTo(expected.type());
        assertThat(result.getLocation()).isEqualTo(expected.location());
        assertThat(result.getEvents().size()).isEqualTo(expected.events().size());
        assertThat(result.getSpeed()).isEqualTo(expected.speed());
        assertThat(result.getDirection()).isEqualTo(expected.direction());
        assertThat(result.getVehicleHistory().size()).isEqualTo(expected.vehicleHistory().size());
    }

    void checkVehicles(VehicleResponse result, VehicleRequest expected) {
        assertThat(result.plateNumber()).isEqualTo(expected.plateNumber());
        assertThat(result.capacity()).isEqualTo(expected.capacity());
        assertThat(result.status()).isEqualTo(expected.status());
        assertThat(result.type()).isEqualTo(expected.type());
        assertThat(result.location()).isEqualTo(expected.location());
        assertThat(result.events().size()).isEqualTo(expected.events().size());
        assertThat(result.speed()).isEqualTo(expected.speed());
        assertThat(result.direction()).isEqualTo(expected.direction());
        assertThat(result.vehicleHistory().size()).isEqualTo(expected.vehicleHistory().size());
    }
}