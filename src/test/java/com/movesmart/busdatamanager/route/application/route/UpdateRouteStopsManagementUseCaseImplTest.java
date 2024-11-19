package com.movesmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.application.route.RouteManagementUseCaseImpl;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteRepository;
import com.movessmart.busdatamanager.route.domain.stop.StopRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UpdateRouteStopsManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @Test
    @DisplayName("GIVEN a List of Stops THEN the Route is updated with the list and returned")
    void testUpdateRouteStops() {

        List<String> newStopIds = Arrays.asList("stop1", "stop2", "stop3");
        Route updatedRoute = new Route(route.getId(), route.getName(), newStopIds, route.getSchedules());

        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(stopRepository.existsById(any())).thenReturn(true);
        when(routeRepository.save(any(Route.class))).thenReturn(updatedRoute);

        Route stopRetrieved = routeManagementUseCaseImpl.updateRouteStops(updatedRoute);

        assertThat(stopRetrieved).isEqualTo(updatedRoute);
        assertThat(stopRetrieved.getStopIds()).contains("stop1", "stop2", "stop3");
    }

    @Test
    @DisplayName("GIVEN a List of Stops WHEN one or more Stops do not exist THEN update fails with an exception")
    void testUpdateRouteStopsWithNonExistingStops() {
        List<String> newStopIds = Arrays.asList("stop1", "stop2", "nonExistingStop");

        Route routeToUpdate = new Route(route.getId(), route.getName(), newStopIds, route.getSchedules());

        when(routeRepository.findById(routeToUpdate.getId())).thenReturn(Optional.of(routeToUpdate));

        when(stopRepository.existsById("stop1")).thenReturn(true);
        when(stopRepository.existsById("stop2")).thenReturn(true);
        when(stopRepository.existsById("nonExistingStop")).thenReturn(false);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.updateRouteStops(routeToUpdate));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Stop")
                .hasMessageContaining("nonExistingStop");
    }

    @Test
    @DisplayName("GIVEN a List of Stops WHEN the Route does not exist THEN update fails with an exception")
    void testUpdateRouteStopsWithNonExistingRoute() {
        List<String> newStopIds = Arrays.asList("stop1", "stop2", "stop3");

        Route routeToUpdate = new Route("nonExistingRouteId", "TestRoute", newStopIds, route.getSchedules());

        when(routeRepository.findById(routeToUpdate.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.updateRouteStops(routeToUpdate));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Route")
                .hasMessageContaining("nonExistingRouteId");
    }

    @Test
    @DisplayName("GIVEN a List of Stops with duplicates THEN duplicates are removed before updating the Route")
    void testUpdateRouteStopsWithDuplicates() {
        List<String> newStopIds = Arrays.asList("stop1", "stop2", "stop2", "stop3");
        List<String> expectedStopIds = Arrays.asList("stop1", "stop2", "stop3");

        Route updatedRoute = new Route(route.getId(), route.getName(), newStopIds, route.getSchedules());

        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(stopRepository.existsById(any())).thenReturn(true);
        when(routeRepository.save(any(Route.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Route result = routeManagementUseCaseImpl.updateRouteStops(updatedRoute);

        assertThat(result.getStopIds()).containsExactlyElementsOf(expectedStopIds);
    }
}
