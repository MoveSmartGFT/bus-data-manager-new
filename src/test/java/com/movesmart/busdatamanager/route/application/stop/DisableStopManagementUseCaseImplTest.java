package com.movesmart.busdatamanager.route.application.stop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movessmart.busdatamanager.core.exception.EntityStatusException;
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
public class DisableStopManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @Test
    @DisplayName("GIVEN a stop id THEN is disabled and returned")
    void testDisableStop() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.findEnabledStopById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.save(stop)).thenReturn(stop);

        Stop stopRetrieved = stopManagementUseCaseImpl.disable(stop.getId());

        assertThat(stopRetrieved).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to disable WHEN it does not exist THEN an exception is thrown")
    void testDisableStopDoesNotExist() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.disable(stop.getId()));

        assertThat(throwable).isInstanceOf(EntityNotFoundException.class).hasMessageContainingAll("Stop", stop.getId());
    }

    @Test
    @DisplayName("GIVEN a stop to disable WHEN it is already disabled THEN an exception is thrown")
    void testDisableStopAlreadyDisabled() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.findEnabledStopById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.disable(stop.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityStatusException.class)
                .hasMessageContainingAll("Stop", stop.getId(), Stop.Status.Disabled.toString());
    }
}
