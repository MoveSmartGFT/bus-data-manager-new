package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RouteManagementUseCaseImpl implements RouteManagementUseCase {

    private final RouteRepository routeRepository;
    private final StopRepository stopRepository;

    public RouteManagementUseCaseImpl(RouteRepository routeRepository, StopRepository stopRepository) {
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
    }

    /**
     * @param route data
     * @return Route
     */
    @Override
    public Route create(Route route) {
        log.info("Attempting to create Route with id: {}", route.getId());

        if (routeRepository.existsById(route.getId())) {
            log.warn("Route with id: {} already exists", route.getId());
            throw new EntityAlreadyExistsException(ROUTE, route.getId());
        }

        List<String> stopIds = route.getStopIds();
        for (String stopId : stopIds) {
            if (!stopRepository.existsById(stopId)) {
                log.warn("Stop with id: {} does not exists", stopId);
                throw new EntityNotFoundException("Stop", stopId);
            }
        }

        Route savedRoute = routeRepository.save(route);
        log.info("Route with ID: {} successfully created", route.getId());

        return savedRoute;
    }

    /**
     * @see RouteManagementUseCase#get(String)
     */
    public Route get(String routeId) {
        return routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(ROUTE, routeId));
    }

    /**
     * Get
     * @see RouteManagementUseCase#getStopIdsByRouteId(String)
     */
    public List<String> getStopIdsByRouteId(String routeId) {
        log.info("Returning stopsIds from routeId: {}", routeId);
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(ROUTE, routeId));

        return route.getStopIds();
    }
}
