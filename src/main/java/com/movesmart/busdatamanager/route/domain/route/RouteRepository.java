package com.movesmart.busdatamanager.route.domain.route;

import lombok.Generated;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

@Generated
@Repository
public interface RouteRepository
        extends MongoRepository<Route, String> {

    /**
     * Searches a Route by id and status
     * @param id of route
     * @param status of route
     */
    Optional<Route> findByIdAndStatus(String id, Route.Status status);

    /**
     * Searches enabled Route by id
     * @param id of route
     */
    default Optional<Route> findEnabledRouteById(String id) {
        return findByIdAndStatus(id, Route.Status.Enabled);
    }

    /**
     * Searches disabled Route by id
     * @param id of route
     */
    default Optional<Route> findDisabledRouteById(String id) {
        return findByIdAndStatus(id, Route.Status.Disabled);
    }
}
