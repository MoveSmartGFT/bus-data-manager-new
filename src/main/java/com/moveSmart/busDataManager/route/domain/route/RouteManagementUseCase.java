package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.route.domain.stop.Stop;

import java.util.List;

public interface RouteManagementUseCase {

    String ROUTE = "Route";

    /**
     * Creates a new Route
     * @param route data
     * @return Route
     */
    Route create (Route route);

    /**
     * Returns list of Stops belonging to a Route
     * @param routeId Identifier of the Route
     */
    List<Stop> getStops(String routeId);
}
