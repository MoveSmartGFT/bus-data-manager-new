package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RouteManagementUseCaseImpl implements RouteManagementUseCase {

    private final RouteRepository routeRepository;

    public RouteManagementUseCaseImpl(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    /**
     * @param route data
     * @return Route
     */

    @Override
    public Route create(Route route) {
        if (routeRepository.existsById(route.getId()))
            throw new EntityAlreadyExistsException(ROUTE,  route.getId());

        return routeRepository.save(route);
    }

    /**
     * @see RouteManagementUseCase#get(String)
     */
    public Route get(String routeId) {
        return routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(ROUTE, routeId));
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
