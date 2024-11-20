package com.movesmart.busdatamanager.route.infraestructure.api.stop;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.StopController;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopResponse;
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
public class EnableStopControllerTest {

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
            "GIVEN a stop enable request is received WHEN the stop exists THEN returns stop object enabled and status 200")
    void testEnable() throws Exception {
        StopResponse stopResponse = StopResponse.fromStop(stop);

        when(stopManagementUseCase.enable(any())).thenReturn(stop);

        mockMvc.perform(patch(
                        StopController.STOP_PATH + StopController.STOP_ID_PATH + StopController.STOP_ENABLE_PATH,
                        stop.getId()))
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT)
                        .isEqualTo(objectMapper.writeValueAsString(stopResponse)));
    }

    @Test
    @DisplayName("GIVEN a stop enable request is received WHEN the stop does not exist THEN returns status 404")
    void testEnableStopDoesNotExist() throws Exception {
        when(stopManagementUseCase.enable(any())).thenThrow(new EntityNotFoundException("Stop", stop.getId()));

        mockMvc.perform(patch(
                        StopController.STOP_PATH + StopController.STOP_ID_PATH + StopController.STOP_ENABLE_PATH,
                        stop.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a stop enable request is received WHEN the stop is already enabled THEN returns status 409")
    void testEnableStopAlreadyEnabled() throws Exception {
        when(stopManagementUseCase.enable(any()))
                .thenThrow(new EntityStatusException("Stop", stop.getId(), Stop.Status.Enabled.toString()));

        mockMvc.perform(patch(
                        StopController.STOP_PATH + StopController.STOP_ID_PATH + StopController.STOP_ENABLE_PATH,
                        stop.getId()))
                .andExpect(status().isNotFound());
    }
}
