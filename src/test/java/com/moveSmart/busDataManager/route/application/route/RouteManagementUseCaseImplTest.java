package com.moveSmart.busDataManager.route.application.route;

import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteRepository;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
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

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN the route exists THEN returns stop list")
    void getStops() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.ofNullable(route));

        List<Stop> stopsRetrieved = routeManagementUseCaseImpl.getStops(route.getId());

        assertThat(stopsRetrieved).isEqualTo(route.getStops());
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a stop list WHEN route does not exist THEN returns an exception")
    void getStopsRouteDoesNotExist() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> routeManagementUseCaseImpl.getStops(route.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", route.getId());
    }
}