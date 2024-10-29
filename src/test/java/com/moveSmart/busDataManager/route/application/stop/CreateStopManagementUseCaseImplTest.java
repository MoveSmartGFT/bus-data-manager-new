package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
import com.moveSmart.busDataManager.route.RouteInstancioModels;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.domain.stop.StopRepository;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.StopRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class CreateStopManagementUseCaseImplTest {

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
        StopRequest newStop = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        when(stopRepository.findByName(newStop.name())).thenReturn(Optional.empty());
        when(stopRepository.save(any())).thenReturn(stop);

        Stop stopCreated = stopManagementUseCaseImpl.create(newStop);

        assertThat(stopCreated).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to create WHEN already exists THEN returns an exception")
    void testStopCreateAlreadyExists() {
        StopRequest newStop = Instancio.create(RouteInstancioModels.STOP_REQUEST_MODEL);

        when(stopRepository.findByName(newStop.name())).thenReturn(Optional.of(stop));

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.create(newStop));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Stop", newStop.name());
    }
}
