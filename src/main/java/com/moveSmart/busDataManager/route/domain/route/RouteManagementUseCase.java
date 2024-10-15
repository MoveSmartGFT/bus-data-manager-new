package com.moveSmart.busDataManager.route.domain.route;

public interface RouteManagementUseCase {

    String ROUTE = "Route";

    /**
     * Creates a new Route
     * @param route data
     * @return Route
     */
    Route create (Route route);
}
