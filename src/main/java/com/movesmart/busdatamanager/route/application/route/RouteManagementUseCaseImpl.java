package com.movesmart.busdatamanager.route.application.route;

import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
import com.movesmart.busdatamanager.route.domain.Schedule;
import com.movesmart.busdatamanager.route.domain.stop.StopRepository;
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

        checkStopsExist(route.getStopIds());

        List<Schedule> validatedSchedules = validateSchedules(route.getSchedules());

        Route validatedRoute = new Route(route.getId(), route.getName(), route.getStopIds(), validatedSchedules);

        Route savedRoute = routeRepository.save(validatedRoute);
        log.info("Route with ID: {} successfully created", route.getId());

        return savedRoute;
    }

    /**
     * @see RouteManagementUseCase#get(String)
     */
    public Route get(String routeId) {
        log.info("Searching route with id: {}", routeId);

        return routeRepository.findById(routeId).orElseThrow(() -> new EntityNotFoundException(ROUTE, routeId));
    }

    /**
     * @see RouteManagementUseCase#getAll()
     */
    public List<Route> getAll() {
        log.info("Retrieving all routes");

        return routeRepository.findAll();
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

    /**
     * @see RouteManagementUseCase#update(Route)
     */
    public Route update(Route route) {
        log.info("Attempting to update Route with id: {}", route.getId());

        get(route.getId());

        checkStopsExist(route.getStopIds());

        log.info("Found Route with id: {}", route.getId());
        return routeRepository.save(route);
    }

    /**
     * @see RouteManagementUseCase#removeStopIdFromRoutes(String)
     */
    public String removeStopIdFromRoutes(String stopId) {
        List<Route> routesWithStopId = routeRepository.findByStopId(stopId);

        routesWithStopId.stream()
                .peek(route -> route.getStopIds().removeIf(id -> id.equals(stopId)))
                .forEach(routeRepository::save);

        return "Stop with id %s removed from %s routes".formatted(stopId, routesWithStopId.size());
    }

    /**
     * @see RouteManagementUseCase#disable(String)
     */
    public Route disable(String routeId) {
        log.info("Attempting to disable Route with id: {}", routeId);

        get(routeId);
        Route route = routeRepository.findEnabledRouteById(routeId)
                .orElseThrow(() -> new EntityStatusException(ROUTE, routeId, Route.Status.Disabled.toString()));

        log.info("Disabling Route with id: {}", routeId);
        route.disable();

        return routeRepository.save(route);
    }

    /**
     * @see RouteManagementUseCase#enable(String)
     */
    public Route enable(String routeId) {
        log.info("Attempting to enable Route with id: {}", routeId);

        get(routeId);
        Route route = routeRepository.findDisabledRouteById(routeId)
                .orElseThrow(() -> new EntityStatusException(ROUTE, routeId, Route.Status.Enabled.toString()));

        log.info("Enabling Route with id: {}", routeId);
        route.enable();

        return routeRepository.save(route);
    }

    /**
     * @see RouteManagementUseCase#delete(String)
     */
    public Route delete(String routeId) {
        log.info("Attempting to delete Route with id: {}", routeId);

        Route route = get(routeId);

        log.info("Deleting Route with id: {}", routeId);
        routeRepository.delete(route);

        return route;
    }

    public Route updateRouteStops(Route route) {
        log.info("Attempting to update the Stops of the Route with id: {}", route.getId());

        Route existingRoute = get(route.getId());

        checkStopsExist(route.getStopIds());
        existingRoute.updateStopIdList(route.getStopIds());

        log.info("Updated Route with id: {}", existingRoute.getId());
        return routeRepository.save(existingRoute);
    }

    private void checkStopsExist(List<String> stopIds) {
        for (String stopId : stopIds) {
            if (!stopRepository.existsById(stopId)) {
                log.warn("Stop with id: {} does not exists", stopId);
                throw new EntityNotFoundException("Stop", stopId);
            }
        }
    }

    private List<Schedule> validateSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .map(this::validateSchedule)
                .toList();
    }

    private Schedule validateSchedule(Schedule schedule) {
        Schedule.TypeOfDay typeOfDay = schedule.typeOfDay();

        if (typeOfDay != Schedule.TypeOfDay.WEEKDAY && typeOfDay != Schedule.TypeOfDay.WEEKEND) {
            throw new IllegalArgumentException("Invalid typeOfDay format. Expected 'WEEKDAY' or 'WEEKEND'.");
        }
        return schedule;
    }
}
