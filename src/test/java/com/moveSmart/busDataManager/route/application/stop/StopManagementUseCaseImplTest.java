package com.moveSmart.busDataManager.route.application.stop;

import com.moveSmart.busDataManager.core.exception.EntityAlreadyExistsException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class StopManagementUseCaseImplTest {

    @Mock
    private StopRepository stopRepository;

    @InjectMocks
    private StopManagementUseCaseImpl stopManagementUseCase;

    Stop stop = Instancio.create(RouteInstancioModels.STOP_MODEL);

    //-----------------------------------------------------------------------------------------------------------------
    //CREATE METHOD

    @Test
    @DisplayName("GIVEN a stop to create THEN returns stop object and status 201")
    void testStopCreate() {
        when(stopRepository.existsById(stop.getId())).thenReturn(false);
        when(stopRepository.save(stop)).thenReturn(stop);

        Stop stopCreated = stopManagementUseCase.create(stop);

        assertThat(stopCreated).isEqualTo(stop);
    }

    @Test
    @DisplayName("GIVEN a stop to create WHEN already exists THEN returns an exception and status 409")
    void testStopCreateAlreadyExists() {
        when(stopRepository.existsById(stop.getId())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> stopManagementUseCase.create(stop));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Stop", stop.getId());
    }
}
