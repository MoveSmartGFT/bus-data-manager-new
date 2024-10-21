package com.moveSmart.busDataManager.route.infraestructure.api.stop;

import com.moveSmart.busDataManager.core.Fixtures;
import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopManagementUseCase;
import com.moveSmart.busDataManager.route.infrastructure.api.route.RouteController;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.StopController;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static net.javacrumbs.jsonunit.spring.JsonUnitResultMatchers.json;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class StopControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StopManagementUseCase stopManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @BeforeEach
    void setUp() {
        StopController stopController = new StopController(stopManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(stopController);
    }

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE ENDPOINT

    @Test
    @DisplayName("WHEN a stop creation request is received THEN returns stop object and status 201")
    void testStopCreate() throws Exception {
        when(stopManagementUseCase.create(any()))
                .thenReturn(stop);

        mockMvc.perform(
                        post(StopController.STOP_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(stop))
                )
                .andExpect(status().isCreated())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stop)));
    }

    @Test
    @DisplayName("WHEN a stop creation request is received WHEN stop already exists THEN returns status 409")
    void testStopCreateConflict() throws Exception {
        when(stopManagementUseCase.create(any()))
                .thenThrow(new EntityAlreadyExistsException("Stop", stop.getId()));

        mockMvc.perform(
                        post(StopController.STOP_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(stop))
                )
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("WHEN a stop creation request is received WHEN is a bad request THEN returns status 400")
    void testStopCreateBadRequest() throws Exception {
        mockMvc.perform(
                        post(StopController.STOP_PATH)
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content("Stop")
                )
                .andExpect(status().isBadRequest());
    }

    //-----------------------------------------------------------------------------------------------------------------
    // GET ENDPOINT

    @Test
    @DisplayName("GIVEN a stop retrieval request is received WHEN the stop exists THEN returns stop object and status 200")
    void getStop() throws Exception {
        when(stopManagementUseCase.get(any()))
                .thenReturn(stop);

        mockMvc.perform(
                        get(StopController.STOP_PATH+StopController.STOP_ID_PATH, stop.getId())
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stop)));
    }

    @Test
    @DisplayName("GIVEN a stop retrieval request is received WHEN the stop does not exist THEN returns status 404")
    void getStopDoesNotExist() throws Exception {
        when(stopManagementUseCase.get(any()))
                .thenThrow(new EntityNotFoundException("Stop", stop.getId()));

        mockMvc.perform(
                        get(StopController.STOP_PATH+StopController.STOP_ID_PATH, stop.getId())
                )
                .andExpect(status().isNotFound());
    }
}
