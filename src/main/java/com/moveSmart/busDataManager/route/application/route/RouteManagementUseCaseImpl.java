package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        return routeRepository.insert(route);
    }
}
