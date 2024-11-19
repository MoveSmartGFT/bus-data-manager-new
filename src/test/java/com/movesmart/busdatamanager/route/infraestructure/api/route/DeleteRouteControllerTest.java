package com.movesmart.busdatamanager.route.infraestructure.api.route;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movessmart.busdatamanager.route.infrastructure.api.route.RouteController;
import com.movessmart.busdatamanager.route.infrastructure.api.route.dto.RouteResponse;
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

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class DeleteRouteControllerTest {

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
    @DisplayName(
            "GIVEN a route delete request is received WHEN the route exists THEN returns route object deleted and status 200")
    void testDelete() throws Exception {
        RouteResponse routeResponse = RouteResponse.fromRoute(route);

        when(routeManagementUseCase.delete(any())).thenReturn(route);

        mockMvc.perform(delete(RouteController.ROUTE_PATH + RouteController.ROUTE_ID_PATH, route.getId()))
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(routeResponse)));
    }

    @Test
    @DisplayName("GIVEN a route delete request is received WHEN the route does not exist THEN returns status 404")
    void testDeleteRouteDoesNotExist() throws Exception {
        when(routeManagementUseCase.delete(any())).thenThrow(new EntityNotFoundException("Route", route.getId()));

        mockMvc.perform(delete(RouteController.ROUTE_PATH + RouteController.ROUTE_ID_PATH, route.getId()))
                .andExpect(status().isNotFound());
    }
}
