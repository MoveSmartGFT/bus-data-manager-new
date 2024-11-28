package com.movesmart.busdatamanager.vehicle.domain.vehicleHistory;

import lombok.Generated;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Generated
@Repository
public interface VehicleHistoryRepository extends MongoRepository<VehicleHistory, String> {}
