package com.movesmart.busdatamanager.route.infraestructure.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.route.RouteController;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.RouteResponse;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteStopsRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class UpdateRouteListStopsControllerTest {

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
    @DisplayName("GIVEN a Route Stop List update request is received WHEN all the stops exist THEN returns updated route object and status 200")
    void testUpdateRouteStops() throws Exception {
        RouteResponse routeResponse = RouteResponse.fromRoute(route);
        UpdateRouteStopsRequest newRoute = Instancio.create(RouteInstancioModels.UPDATE_ROUTE_STOPS_REQUEST_MODEL);

        Route existingRoute = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

        when(routeManagementUseCase.get(route.getId())).thenReturn(existingRoute);
        when(routeManagementUseCase.updateRouteStops(any())).thenReturn(route);

        mockMvc.perform(
                        patch(RouteController.ROUTE_PATH + RouteController.ROUTE_ID_PATH + RouteController.STOPS_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newRoute))
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(routeResponse)));
    }

    @Test
    @DisplayName("GIVEN a Route Stop List update request is received WHEN one or more stops do not exist THEN returns status 404")
    void testUpdateRouteStopsNotFound() throws Exception {
        UpdateRouteStopsRequest newStop = Instancio.create(RouteInstancioModels.UPDATE_ROUTE_STOPS_REQUEST_MODEL);

        Route existingRoute = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
        when(routeManagementUseCase.get(route.getId())).thenReturn(existingRoute);

        when(routeManagementUseCase.updateRouteStops(any()))
                .thenThrow(new EntityNotFoundException("Stop", "NoExistingStopId"));

        mockMvc.perform(
                        patch(RouteController.ROUTE_PATH + RouteController.ROUTE_ID_PATH + RouteController.STOPS_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(newStop))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a Route Stops update request is received WHEN is a bad request THEN returns status 400")
    void testUpdateRouteBadRequest() throws Exception {
        mockMvc.perform(
                        patch(RouteController.ROUTE_PATH + RouteController.ROUTE_ID_PATH + RouteController.STOPS_PATH, route.getId())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString("NoValid"))
                )
                .andExpect(status().isBadRequest());
    }

}