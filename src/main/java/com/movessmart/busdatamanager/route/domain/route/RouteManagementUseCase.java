package com.movessmart.busdatamanager.route.domain.route;

import java.util.List;

public interface RouteManagementUseCase {

    String ROUTE = "Route";

    /**
     * Creates a new Route
     * @param route data
     * @return Route
     */
    Route create(Route route);

    /**
     * Retrieves a Route using its id
     * @param routeId Identifier of the Route
     * @return Route
     */
    Route get(String routeId);

    /**
     * Retrieves all Routes
     * @return list of routes
     */
    List<Route> getAll();

    /**
     * Returns list of Ids of the Stops belonging to a Route
     * @param routeId Identifier of the Route
     * @return list of routeIds
     */
    List<String> getStopIdsByRouteId(String routeId);

    /**
     * Updates a Route
     * @param route new route
     * @return Route
     */
    Route update(Route route);

    /**
     * Disables a Route
     * @param routeId to disable
     * @return Route
     */
    Route disable(String routeId);

    /**
     * Enables a Route
     * @param routeId to enable
     * @return Route
     */
    Route enable(String routeId);

    /**
     * Deletes a Route
     * @param routeId to delete
     * @return Route
     */
    Route delete(String routeId);

    /**
     * Update the Stop List of one Route
     * @param route to update
     * @return Route
     */
    Route updateRouteStops(Route route);

    /**
     * Deletes a Stop from all routes
     * @param stopId to delete
     */
    String removeStopIdFromRoutes(String stopId);
}
