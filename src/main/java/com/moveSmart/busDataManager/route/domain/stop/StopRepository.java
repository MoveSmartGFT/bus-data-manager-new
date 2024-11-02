package com.moveSmart.busDataManager.route.domain.stop;

import lombok.Generated;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Generated
@Repository
public interface StopRepository extends MongoRepository<Stop, String> {
    Optional<Stop> findByName(String name);
}
