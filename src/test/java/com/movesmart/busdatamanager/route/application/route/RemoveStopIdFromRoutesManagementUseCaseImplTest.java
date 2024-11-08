package com.movesmart.busdatamanager.route.application.route;

import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class RemoveStopIdFromRoutesManagementUseCaseImplTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteManagementUseCaseImpl routeManagementUseCaseImpl;

    private final List<String> stopList = Instancio.ofList(String.class).create();
    private final Route route = Instancio.create(RouteInstancioModels.getRouteModelWithStops(stopList));

    @Test
    @DisplayName("GIVEN a stop id to be removed from routes THEN is removed and returns a message")
    void testRemoveStopIdFromRoute() {
        String stopId = route.getStopIds().get(0);

        when(routeRepository.findByStopId(stopId)).thenReturn(List.of(route));
        when(routeRepository.save(any())).thenReturn(route);

        String messageRetrieved = routeManagementUseCaseImpl.removeStopIdFromRoutes(stopId);

        assertThat(messageRetrieved).isEqualTo("Stop with id %s removed from %s routes"
                .formatted(stopId, 1));
    }

    @Test
    @DisplayName("GIVEN a stop id to be removed from routes WHEN stop id does not exists in any route" +
            "THEN returns a message")
    void testRemoveStopIdFromAnyRoute() {
        String stopId = Instancio.create(String.class);

        when(routeRepository.findByStopId(stopId)).thenReturn(List.of());

        String messageRetrieved = routeManagementUseCaseImpl.removeStopIdFromRoutes(stopId);

        assertThat(messageRetrieved).isEqualTo("Stop with id %s removed from %s routes"
                .formatted(stopId, 0));
    }
}
