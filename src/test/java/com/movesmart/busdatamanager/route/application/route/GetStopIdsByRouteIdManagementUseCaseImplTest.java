package com.movesmart.busdatamanager.route.application.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.application.route.RouteManagementUseCaseImpl;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteRepository;
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
public class GetStopIdsByRouteIdManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

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
}
