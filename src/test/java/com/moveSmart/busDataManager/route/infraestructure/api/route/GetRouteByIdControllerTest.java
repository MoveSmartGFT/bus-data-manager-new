package com.moveSmart.busDataManager.route.infraestructure.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.core.Fixtures;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.route.RouteController;
import net.javacrumbs.jsonunit.core.Option;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GetRouteByIdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @BeforeEach
    void setUp() {
        RouteController routeController = new RouteController(routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(routeController);
    }

    @Test
    @DisplayName("GIVEN a route retrieval request is received WHEN the route exists THEN returns route object and status 200")
    void testGetRoute() throws Exception {
        when(routeManagementUseCase.get(any()))
                .thenReturn(route);

        mockMvc.perform(
                        get(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, route.getId())
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(route)));
    }

    @Test
    @DisplayName("GIVEN a route retrieval request is received WHEN the route does not exist THEN returns status 404")
    void testGetRouteDoesNotExist() throws Exception {
        when(routeManagementUseCase.get(any()))
                .thenThrow(new EntityNotFoundException("Route", route.getId()));

        mockMvc.perform(
                        get(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, route.getId())
                )
                .andExpect(status().isNotFound());
    }
}