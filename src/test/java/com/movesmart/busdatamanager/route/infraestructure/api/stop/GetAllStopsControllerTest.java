package com.movesmart.busdatamanager.route.infraestructure.api.stop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movesmart.busdatamanager.core.Fixtures;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.domain.stop.StopManagementUseCase;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.StopController;
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
public class GetAllStopsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StopManagementUseCase stopManagementUseCase;

    private final ObjectMapper objectMapper = Fixtures.setupObjectMapper();

    @BeforeEach
    void setUp() {
        StopController stopController = new StopController(stopManagementUseCase);
        mockMvc = Fixtures.setupMockMvc(stopController);
    }

    @Test
    @DisplayName("GIVEN all stop retrieval request is received THEN returns stop list and status 200")
    void testGetStop() throws Exception {
        List<Stop> stopList = Instancio.ofList(RouteInstancioModels.STOP_MODEL).create();

        when(stopManagementUseCase.getAll())
                .thenReturn(stopList);

        mockMvc.perform(
                        get(StopController.STOP_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(stopList)));
    }

    @Test
    @DisplayName("GIVEN all stop retrieval request is received WHEN there are no stops THEN returns empty stop list and status 200")
    void testGetStopDoesNotExist() throws Exception {
        when(stopManagementUseCase.getAll())
                .thenReturn(List.of());

        mockMvc.perform(
                        get(StopController.STOP_PATH)
                )
                .andExpect(status().isOk())
                .andExpect(json().when(Option.TREATING_NULL_AS_ABSENT).isEqualTo(objectMapper.writeValueAsString(List.of())));

    }
}
