package com.movesmart.busdatamanager.vehicle.application.vehicleHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.VehicleSender;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryRepository;
import java.util.Optional;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class CreateVehicleHistoryManagementUseCaseImplTest {

    @Mock
    private VehicleHistoryRepository vehicleHistoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleSender send;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private VehicleHistoryManagementUseCaseImpl vehicleHistoryManagementUseCaseImpl;

    private final VehicleHistory vehicleHistory = Instancio.create(VehicleInstancioModels.VEHICLE_HISTORY_MODEL);

    @Test
    @DisplayName("GIVEN a vehicleHistory to create THEN returns vehicleHistory object")
    void testVehicleHistoryCreate() {
        when(vehicleHistoryRepository.findById(vehicleHistory.getId())).thenReturn(Optional.empty());
        when(vehicleRepository.existsById(vehicleHistory.getVehicleId())).thenReturn(true);
        when(vehicleHistoryRepository.save(any())).thenReturn(vehicleHistory);

        //        doAnswer(invocation -> {
        //            RouteValidationEvent event = invocation.getArgument(0);
        //            event.setValidated(true);
        //            return null;
        //        })
        //                .when(eventPublisher)
        //                .publishEvent(any(RouteValidationEvent.class));

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        VehicleHistory vehicleHistoryCreated = vehicleHistoryManagementUseCaseImpl.create(vehicleHistory);

        assertThat(vehicleHistoryCreated).isEqualTo(vehicleHistory);
        verify(send).sendMessage(messageCaptor.capture());
        String capturedMessage = messageCaptor.getValue();
        assertThat(capturedMessage)
                .isEqualTo(String.format("VehicleHistory created with ID: %s", vehicleHistory.getId()));
    }

    @Test
    @DisplayName("GIVEN a vehicleHistory to create WHEN already exists THEN returns an exception")
    void testVehicleHistoryCreateAlreadyExists() {
        when(vehicleHistoryRepository.findById(vehicleHistory.getId())).thenReturn(Optional.of(vehicleHistory));

        Throwable throwable = catchThrowable(() -> vehicleHistoryManagementUseCaseImpl.create(vehicleHistory));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("VehicleHistory", vehicleHistory.getId());
    }

    @Test
    @DisplayName(
            "GIVEN a vehicleHistory to create WHEN the vehicle associated does not exist THEN throws EntityNotFoundException")
    void testVehicleHistoryNonExistingVehicle() {
        when(vehicleHistoryRepository.findById(vehicleHistory.getId())).thenReturn(Optional.empty());
        when(vehicleRepository.existsById(vehicleHistory.getVehicleId())).thenReturn(false);

        Throwable throwable = catchThrowable(() -> vehicleHistoryManagementUseCaseImpl.create(vehicleHistory));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Vehicle", vehicleHistory.getVehicleId());
    }
}

    /* @Test
    @DisplayName(
            "GIVEN a vehicleHistory to create WHEN the route associated does not exist THEN throws EntityNotValid exception")
    void testVehicleHistoryNonExistingRoute() {
        when(vehicleHistoryRepository.findById(vehicleHistory.getId())).thenReturn(Optional.empty());
        when(vehicleRepository.existsById(vehicleHistory.getVehicleId())).thenReturn(true);

        doAnswer(invocation -> {
                    RouteValidationEvent event = invocation.getArgument(0);
                    event.setValidated(false);
                    return null;
                })
                .when(eventPublisher)
                .publishEvent(any(RouteValidationEvent.class));

        Throwable throwable = catchThrowable(() -> vehicleHistoryManagementUseCaseImpl.create(vehicleHistory));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Route", vehicleHistory.getRouteId());
    } */
