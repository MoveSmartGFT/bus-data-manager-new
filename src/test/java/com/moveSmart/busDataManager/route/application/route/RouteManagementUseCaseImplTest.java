package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import com.moveSmart.busDataManager.route.domain.schedule.Schedule;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE METHOD

    @Test
    @DisplayName("GIVEN a route to create THEN returns route object and status 201")

    void testRouteCreate() {
        when(routeRepository.existsById(route.getId())).thenReturn(false);
        when(stopRepository.existsById(any())).thenReturn(true);
        when(routeRepository.save(any(Route.class))).thenReturn(route);

        Route routeCreated = routeManagementUseCaseImpl.create(route);

        assertThat(routeCreated).isEqualTo(route);
    }


    @Test
    @DisplayName("GIVEN a route to create WHEN already exists THEN returns an exception and status 409")
    void testRouteCreateAlreadyExists() {
        when(routeRepository.existsById(route.getId())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.create(route));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    @Test
    @DisplayName("GIVEN a route with non-existing stops WHEN creating THEN returns an exception and status 404")
    void testRouteCreateWithNonExistingStops() {
        String nonExistingStopId = "NoStop";
        Route routeWithNonExistingStopId = Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(nonExistingStopId)));

        when(routeRepository.existsById(routeWithNonExistingStopId.getId())).thenReturn(false);
        when(stopRepository.existsById(nonExistingStopId)).thenReturn(false);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.create(routeWithNonExistingStopId));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", nonExistingStopId);
    }

    @Test
    @DisplayName("GIVEN a route with invalid schedule WHEN creating THEN returns an exception")
    void testRouteCreateWithInvalidSchedule() {
        Schedule invalidSchedule = new Schedule(null, LocalDateTime.now(), LocalDateTime.now().plusHours(1), 15);
        Route routeWithInvalidSchedule = new Route(route.getId(), route.getName(), route.getStopIds(), List.of(invalidSchedule));

        when(routeRepository.existsById(routeWithInvalidSchedule.getId())).thenReturn(false);
        when(stopRepository.existsById(any())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.create(routeWithInvalidSchedule));

        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid typeOfDay format. Expected 'WEEKDAY' or 'WEEKEND'.");
    }

    //-----------------------------------------------------------------------------------------------------------------
    //GET METHOD

    @Test
    @DisplayName("GIVEN we try to get a Route WHEN it exist THEN a Route is received")
    void testGetRoute() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));

        Route routeRetrieved = routeManagementUseCaseImpl.get(route.getId());

        assertThat(routeRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a Route WHEN it does not exist THEN an exception is thrown")
    void testGetRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.get(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //GET METHOD

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN the route exists THEN returns stop list")
    void testGetStopIdsByRouteId() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));

        List<String> stopsRetrieved = routeManagementUseCaseImpl.getStopIdsByRouteId(route.getId());

        assertThat(stopsRetrieved).isEqualTo(route.getStopIds());
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN route does not exist THEN returns an exception")
    void testGetStopIdsByRouteIdDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.getStopIdsByRouteId(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //UPDATE METHOD

    @Test
    @DisplayName("GIVEN a route THEN is updated and returned")
    void testUpdateRoute() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(stopRepository.existsById(any())).thenReturn(true);
        when(routeRepository.save(route)).thenReturn(route);

        Route stopRetrieved = routeManagementUseCaseImpl.update(route);

        assertThat(stopRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN a route WHEN it does not exist THEN an exception is thrown")
    void testUpdateRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.update(route));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    @Test
    @DisplayName("GIVEN a route with non-existing stops WHEN update THEN returns an exception and status 404")
    void testRouteUpdateWithNonExistingStops() {
        String nonExistingStopId = "NoStop";
        Route routeWithNonExistingStopId = Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(nonExistingStopId)));

        when(routeRepository.findById(routeWithNonExistingStopId.getId())).thenReturn(Optional.of(routeWithNonExistingStopId));
        when(stopRepository.existsById(nonExistingStopId)).thenReturn(false);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.update(routeWithNonExistingStopId));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", nonExistingStopId);
    }
}
