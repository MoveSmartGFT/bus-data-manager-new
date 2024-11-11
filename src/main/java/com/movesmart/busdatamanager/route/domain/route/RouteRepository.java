package com.movesmart.busdatamanager.route.domain.route;

import lombok.Generated;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

@Generated
public interface RouteRepository extends MongoRepository<Route, String> {

    Optional<Route> findByIdAndStatus(String id, Route.Status status);

    default Optional<Route> findEnabledRouteById(String id) {
        return findByIdAndStatus(id, Route.Status.Enabled);
    }

    default Optional<Route> findDisabledRouteById(String id) {
        return findByIdAndStatus(id, Route.Status.Disabled);
    }

    List<Route> findByStopIdsContaining(String stopId);
}