package com.movessmart.busdatamanager.route.domain.route;

import java.util.List;
import java.util.Optional;
import lombok.Generated;
import org.springframework.data.mongodb.repository.MongoRepository;

@Generated
public interface RouteRepository extends MongoRepository<Route, String> {

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

    /**
     * Searches Routes which contains stopId
     * @param stopId id of the stop
     */
    List<Route> findByStopIds(String stopId);
}
