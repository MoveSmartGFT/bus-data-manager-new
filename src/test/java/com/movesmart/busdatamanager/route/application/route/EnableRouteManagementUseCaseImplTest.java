package com.movesmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.core.exception.EntityStatusException;
import com.movessmart.busdatamanager.route.application.route.RouteManagementUseCaseImpl;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteRepository;
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
public class EnableRouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @Test
    @DisplayName("GIVEN a route id THEN is enabled and returned")
    void testEnableRoute() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findDisabledRouteById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.save(route)).thenReturn(route);

        Route stopRetrieved = routeManagementUseCaseImpl.enable(route.getId());

        assertThat(stopRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN a route to enable WHEN it does not exist THEN an exception is thrown")
    void testEnableRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.enable(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    @Test
    @DisplayName("GIVEN a route to enable WHEN it is already enabled THEN an exception is thrown")
    void testEnableRouteAlreadyEnabled() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findDisabledRouteById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.enable(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityStatusException.class)
                .hasMessageContainingAll("Route", route.getId(), Route.Status.Enabled.toString());
    }
}
