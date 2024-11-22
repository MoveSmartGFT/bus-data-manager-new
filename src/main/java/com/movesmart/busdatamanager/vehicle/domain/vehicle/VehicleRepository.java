package com.movesmart.busdatamanager.vehicle.domain.vehicle;

import java.util.Optional;
import lombok.Generated;
import org.springframework.data.mongodb.repository.MongoRepository;

@Generated
public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    /**
     * Searches a Vehicle by id and status
     * @param plateNumber of vehicle
     */
    Optional<Vehicle> findById(String plateNumber);
}
