package com.movesmart.busdatamanager.route.application.route;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class DeleteRouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @Test
    @DisplayName("GIVEN a route id THEN is deleted and returned")
    void testDeleteRoute() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));

        Route stopRetrieved = routeManagementUseCaseImpl.delete(route.getId());

        assertThat(stopRetrieved).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN a route to delete WHEN it does not exist THEN an exception is thrown")
    void testDeleteRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.delete(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }
}