package com.movessmart.busdatamanager.route.infraestructure.api.stop;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movessmart.busdatamanager.core.Fixtures;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movessmart.busdatamanager.route.domain.stop.Stop;
import com.movessmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movessmart.busdatamanager.route.infrastructure.api.stop.StopController;
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
public class GetStopByIdControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StopManagementUseCase stopManagementUseCase;

    @Mock
    private RouteManagementUseCase routeManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @BeforeEach
    void setUp() {
        StopController stopController = new StopController(stopManagementUseCase, routeManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(stopController);
    }

    @Test
    @DisplayName(
            "GIVEN a stop retrieval request is received WHEN the stop exists THEN returns stop object and status 200")
    void testGetStop() throws Exception {
        when(stopManagementUseCase.get(any())).thenReturn(stop);

        mockMvc.perform(get(StopController.STOP_PATH + StopController.STOP_ID_PATH, stop.getId()))
                .andExpect(status().isOk())
                .andExpect(
                        json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stop)));
    }

    @Test
    @DisplayName("GIVEN a stop retrieval request is received WHEN the stop does not exist THEN returns status 404")
    void testGetStopDoesNotExist() throws Exception {
        when(stopManagementUseCase.get(any())).thenThrow(new EntityNotFoundException("Stop", stop.getId()));

        mockMvc.perform(get(StopController.STOP_PATH + StopController.STOP_ID_PATH, stop.getId()))
                .andExpect(status().isNotFound());
    }
}
