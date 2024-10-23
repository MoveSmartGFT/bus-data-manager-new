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
    Route create(Route route);

    /**
     * Returns list of Ids of the Stops belonging to a Route
     * @param routeId Identifier of the Route
     */
    List<String> getStopIdsByRouteId(String routeId);
    List<String> getStopIds(String routeId);

    Route get(String routeId);

}
