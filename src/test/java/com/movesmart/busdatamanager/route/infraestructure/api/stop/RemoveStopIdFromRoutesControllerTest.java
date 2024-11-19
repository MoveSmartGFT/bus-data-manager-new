package com.movesmart.busdatamanager.route.infraestructure.api.stop;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movessmart.busdatamanager.route.domain.stop.Stop;
import com.movessmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movessmart.busdatamanager.route.infrastructure.api.stop.StopController;
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
public class RemoveStopIdFromRoutesControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StopManagementUseCase stopManagementUseCase;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @BeforeEach
    void setUp() {
        StopController stopController = new StopController(stopManagementUseCase, routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(stopController);
    }

    @Test
    @DisplayName("GIVEN a stop id to be removed from routes THEN is removed and returns a message")
    void testRemoveStopIdFromRoute() throws Exception {
        when(routeManagementUseCase.removeStopIdFromRoutes(any()))
                .thenReturn("Stop with id %s removed from %s routes".formatted(stop.getId(), 0));

        mockMvc.perform(patch(
                        StopController.STOP_PATH + StopController.STOP_ID_PATH + StopController.ROUTE_PATH,
                        stop.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Stop with id %s removed from %s routes".formatted(stop.getId(), 0)));
    }
}
