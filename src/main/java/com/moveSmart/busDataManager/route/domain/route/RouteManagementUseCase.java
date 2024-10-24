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
     */
    Route get(String routeId);

    /**
     * Returns list of Ids of the Stops belonging to a Route
     * @param routeId Identifier of the Route
     */
    List<String> getStopIdsByRouteId(String routeId);
}
