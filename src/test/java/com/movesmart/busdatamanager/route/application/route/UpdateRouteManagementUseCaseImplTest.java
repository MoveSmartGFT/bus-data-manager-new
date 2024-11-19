package com.movesmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.application.route.RouteManagementUseCaseImpl;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteRepository;
import com.movessmart.busdatamanager.route.domain.stop.StopRepository;
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
public class UpdateRouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

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
        Route routeWithNonExistingStopId =
                Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(nonExistingStopId)));

        when(routeRepository.findById(routeWithNonExistingStopId.getId()))
                .thenReturn(Optional.of(routeWithNonExistingStopId));
        when(stopRepository.existsById(nonExistingStopId)).thenReturn(false);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.update(routeWithNonExistingStopId));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", nonExistingStopId);
    }
}
