package com.movesmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
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
public class DisableRouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @Test
    @DisplayName("GIVEN a route id THEN is disabled and returned")
    void testDisableRoute() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findEnabledRouteById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.save(route)).thenReturn(route);

        Route routeRetrieved = routeManagementUseCaseImpl.disable(route.getId());

        assertThat(routeRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN a route to disable WHEN it does not exist THEN an exception is thrown")
    void testDisableRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.disable(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }

    @Test
    @DisplayName("GIVEN a route to disable WHEN it is already disabled THEN an exception is thrown")
    void testDisableRouteAlreadyDisabled() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findEnabledRouteById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.disable(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityStatusException.class)
                .hasMessageContainingAll("Route", route.getId(), Route.Status.Disabled.toString());
    }
}
