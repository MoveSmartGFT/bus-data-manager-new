package com.moveSmart.busDataManager.route.infraestructure.api.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moveSmart.busDataManager.core.Fixtures;
import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.route.RouteManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.route.RouteController;
import com.moveSmart.busDataManager.route.infrastructure.api.route.dto.UpdateRouteRequest;
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

import java.util.List;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RouteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);
    private final List<String> stopIdList = Instancio.createList(String.class);

    @BeforeEach
    void setUp() {
        RouteController routeController = new RouteController(routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(routeController);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a route creation request is received THEN returns route object and status 201")
    void testRouteCreate() throws Exception {
        when(routeManagementUseCase.create(any()))
                .thenReturn(route);

        mockMvc.perform(
                        post(RouteController.ROUTE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(route))
                )
                .andExpect(status().isCreated())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(route)));
    }

    @Test
    @DisplayName("WHEN a route creation request is received WHEN route already exists THEN returns status 409")
    void testRouteCreateConflict() throws Exception {
        when(routeManagementUseCase.create(any()))
                .thenThrow(new EntityAlreadyExistsException("Route", route.getId()));

        mockMvc.perform(
                        post(RouteController.ROUTE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(route))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("WHEN a route creation request is received WHEN is a bad request THEN returns status 400")
    void testRouteCreateBadRequest() throws Exception {
        mockMvc.perform(
                        post(RouteController.ROUTE_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("Route")
                )
                .andExpect(status().isBadRequest());
    }

    //-----------------------------------------------------------------------------------------------------------------
    // GET ENDPOINT

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

    //-----------------------------------------------------------------------------------------------------------------
    // GET STOPS ENDPOINT

    @Test
    @DisplayName("GIVEN a stops retrieval request is received WHEN the route exists THEN returns stop list object and status 200")
    void testGetStops() throws Exception {
        when(routeManagementUseCase.getStopIdsByRouteId(any()))
                .thenReturn(stopIdList);

        mockMvc.perform(
                        get(RouteController.ROUTE_PATH+RouteController.ROUTE_ID_PATH+RouteController.STOPS_PATH, route.getId())
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stopIdList)));
    }

    //-----------------------------------------------------------------------------------------------------------------
    // UPDATE ENDPOINT

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
