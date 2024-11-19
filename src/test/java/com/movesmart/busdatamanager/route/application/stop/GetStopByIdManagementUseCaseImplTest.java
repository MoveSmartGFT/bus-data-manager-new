package com.movesmart.busdatamanager.route.application.stop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.route.application.stop.StopManagementUseCaseImpl;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movessmart.busdatamanager.route.domain.stop.Stop;
import com.movessmart.busdatamanager.route.domain.stop.StopRepository;
import java.util.Optional;
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
public class GetStopByIdManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @Test
    @DisplayName("GIVEN a stopId THEN stop is returned")
    void testGetStop() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));

        Stop stopRetrieved = stopManagementUseCaseImpl.get(stop.getId());

        assertThat(stopRetrieved).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop Id WHEN it does not exist THEN an exception is thrown")
    void testGetStopDoesNotExist() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.get(stop.getId()));

        assertThat(throwable).isInstanceOf(EntityNotFoundException.class).hasMessageContainingAll("Stop", stop.getId());
    }
}
