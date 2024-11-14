package com.movesmart.busdatamanager.route.domain.stop;

import java.util.Optional;
import lombok.Generated;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Generated
@Repository
public interface StopRepository extends MongoRepository<Stop, String> {

    /**
     * Searches a Stop name
     * @param name of route
     */
    Optional<Stop> findByName(String name);
}
