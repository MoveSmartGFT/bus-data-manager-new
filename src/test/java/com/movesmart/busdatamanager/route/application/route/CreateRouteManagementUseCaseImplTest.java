package com.movessmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteRepository;
import com.movessmart.busdatamanager.route.domain.stop.StopRepository;
import java.util.List;
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
public class CreateRouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

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
        Route routeWithNonExistingStopId =
                Instancio.create(RouteInstancioModels.getRouteModelWithStops(List.of(nonExistingStopId)));

        when(routeRepository.existsById(routeWithNonExistingStopId.getId())).thenReturn(false);
        when(stopRepository.existsById(nonExistingStopId)).thenReturn(false);

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.create(routeWithNonExistingStopId));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", nonExistingStopId);
    }
}
