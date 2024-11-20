package com.movesmart.busdatamanager.vehicle.it;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movessmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movessmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.UpdateVehicleRequest;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movessmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleResponse;
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
        VehicleRequest secondVehicleRequest = Instancio.create(VehicleInstancioModels.VEHICLE_REQUEST_MODEL);

        MvcResult createVehicleResponse = createVehicleRequest(firstVehicleRequest);
        assertThat(HttpStatus.valueOf(createVehicleResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        VehicleResponse createdVehicle =
                objectMapper.readValue(createVehicleResponse.getResponse().getContentAsString(), VehicleResponse.class);
        checkVehicles(createdVehicle, firstVehicleRequest);

        MvcResult retrieveVehicleResponse = getVehicleRequest(firstVehicleRequest.plateNumber());
        assertThat(HttpStatus.valueOf(retrieveVehicleResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehicle =
                objectMapper.readValue(retrieveVehicleResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehicle, firstVehicleRequest);

        MvcResult VehicleConflictResponse = createVehicleRequest(firstVehicleRequest);
        assertThat(HttpStatus.valueOf(VehicleConflictResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CONFLICT);
        MvcResult retrieveVehiclePostConflictResponse = getVehicleRequest(firstVehicleRequest.plateNumber());
        assertThat(HttpStatus.valueOf(
                        retrieveVehiclePostConflictResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehiclePostConflict = objectMapper.readValue(
                retrieveVehiclePostConflictResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehiclePostConflict, firstVehicleRequest);

        MvcResult createSecondVehicleResponse = createVehicleRequest(secondVehicleRequest);
        assertThat(HttpStatus.valueOf(createSecondVehicleResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.CREATED);
        VehicleResponse secondCreatedVehicle = objectMapper.readValue(
                createSecondVehicleResponse.getResponse().getContentAsString(), VehicleResponse.class);

        UpdateVehicleRequest vehicleRequest = Instancio.create(VehicleInstancioModels.UPDATE_VEHICLE_REQUEST_MODEL);

        Vehicle firstCreatedVehicle =
                objectMapper.readValue(createVehicleResponse.getResponse().getContentAsString(), Vehicle.class);

        MvcResult updateVehicleRequest = updateVehicleRequest(firstCreatedVehicle.getPlateNumber(), vehicleRequest);
        assertThat(HttpStatus.valueOf(updateVehicleRequest.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);

        Vehicle updatedVehicle =
                objectMapper.readValue(updateVehicleRequest.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(updatedVehicle, vehicleRequest.toVehicle(firstCreatedVehicle.getPlateNumber()));

        MvcResult vehicleRetrievedUpdatedResponse = getVehicleRequest(firstCreatedVehicle.getPlateNumber());
        assertThat(HttpStatus.valueOf(
                        vehicleRetrievedUpdatedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehicleUpdated = objectMapper.readValue(
                vehicleRetrievedUpdatedResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehicleUpdated, vehicleRequest.toVehicle(firstCreatedVehicle.getPlateNumber()));

        MvcResult updatedRouteNotFoundResponse = updateVehicleRequest("Vehicle1", vehicleRequest);
        assertThat(HttpStatus.valueOf(updatedRouteNotFoundResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);

        MvcResult deleteVehicleResponse = deleteVehicleRequest(secondCreatedVehicle.plateNumber());
        assertThat(HttpStatus.valueOf(deleteVehicleResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.OK);
        Vehicle retrievedVehicleDeleted =
                objectMapper.readValue(deleteVehicleResponse.getResponse().getContentAsString(), Vehicle.class);
        checkVehicles(retrievedVehicleDeleted, secondVehicleRequest);
        MvcResult getDeletedResponse = getVehicleRequest(secondCreatedVehicle.plateNumber());
        assertThat(HttpStatus.valueOf(getDeletedResponse.getResponse().getStatus()))
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    void checkVehicles(Vehicle result, VehicleRequest expected) {
        assertThat(result.getPlateNumber()).isEqualTo(expected.plateNumber());
        assertThat(result.getCapacity()).isEqualTo(expected.capacity());
        assertThat(result.getType()).isEqualTo(expected.type());
        assertThat(result.getLocation().latitude())
                .isEqualTo(expected.location().latitude());
        assertThat(result.getLocation().longitude())
                .isEqualTo(expected.location().longitude());
        assertThat(result.getEvents().size()).isEqualTo(expected.events().size());
        assertThat(result.getSpeed()).isEqualTo(expected.speed());
        assertThat(result.getDirection()).isEqualTo(expected.direction());
        assertThat(result.getVehicleHistory().size())
                .isEqualTo(expected.vehicleHistory().size());
    }

    void checkVehicles(Vehicle result, Vehicle expected) {
        assertThat(result.getPlateNumber()).isEqualTo(expected.getPlateNumber());
        assertThat(result.getCapacity()).isEqualTo(expected.getCapacity());
        assertThat(result.getType()).isEqualTo(expected.getType());
        assertThat(result.getLocation().latitude())
                .isEqualTo(expected.getLocation().latitude());
        assertThat(result.getLocation().longitude())
                .isEqualTo(expected.getLocation().longitude());
        assertThat(result.getEvents().size()).isEqualTo(expected.getEvents().size());
        assertThat(result.getSpeed()).isEqualTo(expected.getSpeed());
        assertThat(result.getDirection()).isEqualTo(expected.getDirection());
        assertThat(result.getVehicleHistory().size())
                .isEqualTo(expected.getVehicleHistory().size());
    }

    void checkVehicles(VehicleResponse result, VehicleRequest expected) {
        assertThat(result.plateNumber()).isEqualTo(expected.plateNumber());
        assertThat(result.capacity()).isEqualTo(expected.capacity());
        assertThat(result.type()).isEqualTo(expected.type());
        assertThat(result.location()).isEqualTo(expected.location());
        assertThat(result.events().size()).isEqualTo(expected.events().size());
        assertThat(result.speed()).isEqualTo(expected.speed());
        assertThat(result.direction()).isEqualTo(expected.direction());
        assertThat(result.vehicleHistory().size())
                .isEqualTo(expected.vehicleHistory().size());
    }
}
