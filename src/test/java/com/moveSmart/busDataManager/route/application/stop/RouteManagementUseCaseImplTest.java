package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.application.route.RouteManagementUseCaseImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RouteManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCase;

    Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE METHOD

    @Test
    @DisplayName("GIVEN a route to create THEN returns route object and status 201")

    void testRouteCreate() {
        when(routeRepository.existsById(route.getId())).thenReturn(false);
        when(routeRepository.insert(route)).thenReturn(route);

        Route routeCreated = routeManagementUseCase.create(route);

        assertThat(routeCreated).isEqualTo(route);
    }

    @Test
    @DisplayName("GIVEN a route to create WHEN already exists THEN returns an exception and status 409")
    void testStopCreateAlreadyExists() {
        when(routeRepository.existsById(route.getId())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> routeManagementUseCase.create(route));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Route", route.getId());
    }
}

