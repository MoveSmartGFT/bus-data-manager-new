package com.moveSmart.busDataManager.route.infraestructure.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.core.Fixtures;
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

import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GetAllRoutesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    @BeforeEach
    void setUp() {
        RouteController routeController = new RouteController(routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(routeController);
    }

    @Test
    @DisplayName("GIVEN all route retrieval request is received THEN returns route list and status 200")
    void testGetAllRoutes() throws Exception {
        Route route1 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        Route route2 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        Route route3 = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        List<Route> routeList = List.of(route1, route2, route3);

        when(routeManagementUseCase.getAll())
                .thenReturn(routeList);

        mockMvc.perform(
                        get(RouteController.ROUTE_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(routeList)));
    }

    @Test
    @DisplayName("GIVEN all route retrieval request is received THEN returns route empty list and status 200")
    void testGetAllRoutesEmpty() throws Exception {
        when(routeManagementUseCase.getAll())
                .thenReturn(List.of());

        mockMvc.perform(
                        get(RouteController.ROUTE_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(List.of())));
    }
}
