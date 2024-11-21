package com.movesmart.busdatamanager.vehicle.domain.vehicle;

import lombok.Generated;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Generated
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    /**
     * Searches a Vehicle by id and status
     * @param plateNumber of vehicle
     */
    Optional<Vehicle> findById(String plateNumber);
}
