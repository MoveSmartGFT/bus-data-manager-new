package com.movesmart.busdatamanager.route.infraestructure.api.route;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movessmart.busdatamanager.route.domain.route.Route;
import com.movessmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movessmart.busdatamanager.route.infrastructure.api.route.RouteController;
import com.movessmart.busdatamanager.route.infrastructure.api.route.dto.CreateRouteRequest;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class CreateRouteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final CreateRouteRequest createRouteRequest =
            Instancio.create(RouteInstancioModels.CREATE_ROUTE_REQUEST_MODEL);
    private final Route route = createRouteRequest.toRoute();

    @BeforeEach
    void setUp() {
        RouteController routeController = new RouteController(routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(routeController);
    }

    @Test
    @DisplayName("WHEN a route creation request is received THEN returns route object and status 201")
    void testRouteCreate() throws Exception {
        RouteResponse routeResponse = RouteResponse.fromRoute(route);

        when(routeManagementUseCase.create(any())).thenReturn(route);

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRouteRequest)))
                .andExpect(status().isCreated())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(routeResponse)));
    }

    @Test
    @DisplayName("WHEN a route creation request is received WHEN route already exists THEN returns status 409")
    void testRouteCreateConflict() throws Exception {
        when(routeManagementUseCase.create(any())).thenThrow(new EntityAlreadyExistsException("Route", route.getId()));

        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createRouteRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("WHEN a route creation request is received WHEN is a bad request THEN returns status 400")
    void testRouteCreateBadRequest() throws Exception {
        mockMvc.perform(post(RouteController.ROUTE_PATH)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("Route"))
                .andExpect(status().isBadRequest());
    }
}
