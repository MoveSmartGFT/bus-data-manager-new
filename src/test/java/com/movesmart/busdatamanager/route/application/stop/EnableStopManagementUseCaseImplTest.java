package com.movesmart.busdatamanager.route.application.stop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.domain.stop.StopRepository;
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
public class EnableStopManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    @Test
    @DisplayName("GIVEN a stop id THEN is enabled and returned")
    void testEnableStop() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.findDisabledStopById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.save(stop)).thenReturn(stop);

        Stop stopRetrieved = stopManagementUseCaseImpl.enable(stop.getId());

        assertThat(stopRetrieved).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to enable WHEN it does not exist THEN an exception is thrown")
    void testEnableStopDoesNotExist() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.enable(stop.getId()));

        assertThat(throwable).isInstanceOf(EntityNotFoundException.class).hasMessageContainingAll("Stop", stop.getId());
    }

    @Test
    @DisplayName("GIVEN a stop to enable WHEN it is already disabled THEN an exception is thrown")
    void testEnableStopAlreadyEnabled() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.findDisabledStopById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.enable(stop.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityStatusException.class)
                .hasMessageContainingAll("Stop", stop.getId(), Stop.Status.Enabled.toString());
    }
}
