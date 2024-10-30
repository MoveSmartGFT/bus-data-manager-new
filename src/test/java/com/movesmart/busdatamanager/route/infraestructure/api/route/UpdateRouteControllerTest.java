package com.movesmart.busdatamanager.route.infraestructure.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.route.RouteController;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import net.javacrumbs.jsonunit.core.Option;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UpdateRouteControllerTest {

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
    @DisplayName("GIVEN a route update request is received WHEN the route exists THEN returns route object updated and status 200")
    void testUpdate() throws Exception {
        UpdateRouteRequest newRoute = Instancio.create(RouteInstancioModels.UPDATE_ROUTE_REQUEST_MODEL);

        when(routeManagementUseCase.update(any()))
                .thenReturn(route);

        mockMvc.perform(
                        put(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newRoute))
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(route)));
    }

    @Test
    @DisplayName("GIVEN a route update request is received WHEN the route or a stop does not exist THEN returns status 404")
    void testUpdateRouteDoesNotExist() throws Exception {
        UpdateRouteRequest newRoute = Instancio.create(RouteInstancioModels.UPDATE_ROUTE_REQUEST_MODEL);

        when(routeManagementUseCase.update(any()))
                .thenThrow(new EntityNotFoundException("Route", route.getId()));

        mockMvc.perform(
                        put(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newRoute))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a route update request is received WHEN is a bad request THEN returns status 400")
    void testUpdateRouteBadRequest() throws Exception {
        mockMvc.perform(
                        put(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString("Route"))
                )
                .andExpect(status().isBadRequest());
    }
}
