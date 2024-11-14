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

    /**
     * Searches a Stop by id and status
     * @param id of stop
     * @param status of stop
     */
    Optional<Stop> findByIdAndStatus(String id, Stop.Status status);

    /**
     * Searches enabled Stop by id
     * @param id of stop
     */
    default Optional<Stop> findEnabledStopById(String id) {
        return findByIdAndStatus(id, Stop.Status.Enabled);
    }

    /**
     * Searches disabled Stop by id
     * @param id of stop
     */
    default Optional<Stop> findDisabledStopById(String id) {
        return findByIdAndStatus(id, Stop.Status.Disabled);
    }
}
