package com.moveSmart.busDataManager.route.application.route;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GetAllRoutesManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    @Test
    @DisplayName("GIVEN we try to get all Routes THEN all routes are retrieved")
    void testGetAllRoutes() {
        Route route1 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        Route route2 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        Route route3 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        List<Route> routeList = List.of(route1, route2, route3);

        when(routeRepository.findAll()).thenReturn(routeList);

        List<Route> routeListRetrieved = routeManagementUseCaseImpl.getAll();

        assertThat(routeListRetrieved).isEqualTo(routeList);
    }

    @Test
    @DisplayName("GIVEN we try to get all Routes WHEN there are no routes THEN empty list is retrieved")
    void testGetAllRoutesEmpty() {
        when(routeRepository.findAll()).thenReturn(List.of());

        List<Route> routeListRetrieved = routeManagementUseCaseImpl.getAll();

        assertThat(routeListRetrieved).isEqualTo(List.of());
    }
}
