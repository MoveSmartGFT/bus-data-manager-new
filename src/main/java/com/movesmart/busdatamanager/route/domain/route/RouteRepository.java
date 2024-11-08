package com.movesmart.busdatamanager.route.domain.route;

import lombok.Generated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@Generated
public interface RouteRepository
        extends JpaRepository<Route, String> {

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
    @Query(value = "SELECT r FROM Route r WHERE :stopId MEMBER OF r.stopIds")
    List<Route> findByStopId(@Param("stopId") String stopId);
}
