package com.moveSmart.busDataManager.route.domain.route;

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
}
