package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.core.exception.EntityNotFoundException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class StopManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE METHOD

    @Test
    @DisplayName("GIVEN a stop to create THEN returns stop object")
    void testStopCreate() {
        when(stopRepository.existsById(stop.getId())).thenReturn(false);
        when(stopRepository.save(stop)).thenReturn(stop);

        Stop stopCreated = stopManagementUseCaseImpl.create(stop);

        assertThat(stopCreated).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to create WHEN already exists THEN returns an exception")
    void testStopCreateAlreadyExists() {
        when(stopRepository.existsById(stop.getId())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.create(stop));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Stop", stop.getId());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //GET METHOD

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

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", stop.getId());
    }

    //-----------------------------------------------------------------------------------------------------------------
    //UPDATE METHOD

    @Test
    @DisplayName("GIVEN a stop THEN is updated and returned")
    void testUpdateStop() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));
        when(stopRepository.save(stop)).thenReturn(stop);

        Stop stopRetrieved = stopManagementUseCaseImpl.update(stop);

        assertThat(stopRetrieved).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop WHEN it does not exist THEN an exception is thrown")
    void testUpdateStopDoesNotExist() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.get(stop.getId()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Stop", stop.getId());
    }
}
