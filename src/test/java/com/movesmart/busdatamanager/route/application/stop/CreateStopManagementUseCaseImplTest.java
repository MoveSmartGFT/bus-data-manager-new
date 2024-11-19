package com.movesmart.busdatamanager.route.application.stop;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.movessmart.busdatamanager.core.exception.EntityAlreadyExistsException;
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
public class CreateStopManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCaseImpl;

    private final Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    // -----------------------------------------------------------------------------------------------------------------
    // CREATE METHOD

    @Test
    @DisplayName("GIVEN a stop to create THEN returns stop object")
    void testStopCreate() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.empty());
        when(stopRepository.save(any())).thenReturn(stop);

        Stop stopCreated = stopManagementUseCaseImpl.create(stop);

        assertThat(stopCreated).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to create WHEN already exists THEN returns an exception")
    void testStopCreateAlreadyExists() {
        when(stopRepository.findById(stop.getId())).thenReturn(Optional.of(stop));

        Throwable throwable = catchThrowable(() -> stopManagementUseCaseImpl.create(stop));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Stop", stop.getId());
    }
}
