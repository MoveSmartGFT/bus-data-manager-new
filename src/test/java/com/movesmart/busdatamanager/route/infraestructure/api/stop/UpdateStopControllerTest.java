package com.movesmart.busdatamanager.route.infraestructure.api.stop;

import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.route.RouteManagementUseCase;
import com.movessmart.busdatamanager.route.domain.stop.Stop;
import com.movessmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movessmart.busdatamanager.route.infrastructure.api.stop.StopController;
import com.movessmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
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
public class UpdateStopControllerTest {

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
            "GIVEN a stop update request is received WHEN the stop exists THEN returns stop object updated and status 200")
    void testUpdate() throws Exception {
        StopRequest newStop = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        when(stopManagementUseCase.update(any())).thenReturn(stop);

        mockMvc.perform(put(StopController.STOP_PATH + StopController.STOP_ID_PATH, stop.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newStop)))
                .andExpect(status().isOk())
                .andExpect(
                        json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stop)));
    }

    @Test
    @DisplayName("GIVEN a stop update request is received WHEN the stop does not exist THEN returns status 404")
    void testUpdateStopDoesNotExist() throws Exception {
        StopRequest newStop = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        when(stopManagementUseCase.update(any())).thenThrow(new EntityNotFoundException("Stop", stop.getId()));

        mockMvc.perform(put(StopController.STOP_PATH + StopController.STOP_ID_PATH, stop.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(newStop)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GIVEN a stop update request is received WHEN is a bad request THEN returns status 400")
    void testUpdateStopBadRequest() throws Exception {
        mockMvc.perform(put(StopController.STOP_PATH + StopController.STOP_ID_PATH, stop.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString("Stop")))
                .andExpect(status().isBadRequest());
    }
}
