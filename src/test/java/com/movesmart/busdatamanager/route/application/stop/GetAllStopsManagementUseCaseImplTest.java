package com.movessmart.busdatamanager.route.application.stop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.stop.Stop;
import com.movessmart.busdatamanager.route.domain.stop.StopRepository;
import java.util.List;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GetAllStopsManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    @Test
    @DisplayName("GIVEN we try to get all stops THEN all stops are retrieved")
    void testGetAllStops() {
        List<Stop> stopList = Instancio.ofList(RouteInstancioModels.STOP_MODEL).create();

        when(stopRepository.findAll()).thenReturn(stopList);

        List<Stop> stopListRetrieved = stopManagementUseCaseImpl.getAll();

        assertThat(stopListRetrieved).isEqualTo(stopList);
    }

    @Test
    @DisplayName("GIVEN we try to get all stop WHEN there are no Stop THEN empty list is retrieved")
    void testGetAllStopsEmpty() {
        when(stopRepository.findAll()).thenReturn(List.of());

        List<Stop> stopListRetrieved = stopManagementUseCaseImpl.getAll();

        assertThat(stopListRetrieved).isEqualTo(List.of());
    }
}
