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
        if (routeRepository.existsById(route.getId()))
            throw new EntityAlreadyExistsException(ROUTE,  route.getId());

        List<String> stopIds = route.getStopIds();
        for (String stopId : stopIds) {
            if (!stopRepository.existsById(stopId)) {
                throw new EntityNotFoundException("Stop", stopId);
            }
        }
        return routeRepository.save(route);
    }

    /**
     * Get
     * @see RouteManagementUseCase#getStopIds(String)
     */
    public List<String> getStopIds(String routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(ROUTE, routeId));

        return route.getStopIds();
    }
}
