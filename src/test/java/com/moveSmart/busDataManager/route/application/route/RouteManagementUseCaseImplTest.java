package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
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
    List<Stop> stops = Instancio.create(RouteInstancioModels.STOP_LIST_MODEL);

    Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL(stops));

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE METHOD

    @Test
    @DisplayName("GIVEN a route to create THEN returns route object and status 201")

    void testRouteCreate() {
        // Given
        Route route = new Route();
        route.setStopIds(Arrays.asList("stop1", "stop2"));

        // Mocking repository behavior
        when(routeRepository.existsById(route.getId())).thenReturn(false);
        when(stopRepository.existsById("stop1")).thenReturn(true);
        when(stopRepository.existsById("stop2")).thenReturn(true);
        when(routeRepository.save(route)).thenReturn(route);

        // When
        Route routeCreated = routeManagementUseCaseImpl.create(route);

        // Then
        assertThat(routeCreated).isEqualTo(route);
    }


    @Test
    @DisplayName("GIVEN a route to create WHEN already exists THEN returns an exception and status 409")
    void testStopCreateAlreadyExists() {
        when(routeRepository.existsById(route.getId())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.create(route));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //GET METHOD

    @Test
    @DisplayName("GIVEN we try to get a Route WHEN it exist THEN a Route is received")
    void getStop() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.ofNullable(route));

        Route routeRetrieved = routeManagementUseCaseImpl.get(route.getId());

        assertThat(routeRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a Route WHEN it does not exist THEN an exception is thrown")
    void getStopDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.get(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN the route exists THEN returns stop list")
    void getStops() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.ofNullable(route));

        List<String> stopsRetrieved = routeManagementUseCaseImpl.getStopIds(route.getId());

        assertThat(stopsRetrieved).isEqualTo(route.getStopIds());
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN route does not exist THEN returns an exception")
    void getStopsRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.getStopIds(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }
}
