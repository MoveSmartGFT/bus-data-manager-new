package com.movesmart.busdatamanager.vehicle.application.vehicle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.exception.EntityStatusException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
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
public class ChangeStatusVehicleManagementUseCaseImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleManagementUseCaseImpl vehicleManagementUseCase;

    private final Vehicle vehicle = Instancio.create(VehicleInstancioModels.VEHICLE_MODEL);

    @Test
    @DisplayName("GIVEN a vehicle plateNumber THEN is changed the status and returned")
    void testChangeStatusVehicle() {
        vehicle.setVehicleInService();
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle vehicleRetrieved =
                vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.OutOfService);

        assertThat(vehicleRetrieved).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("GIVEN a vehicle to change the status WHEN transitioning to InService THEN status is updated")
    void testChangeStatusToInService() {
        vehicle.setVehicleInMaintenance();
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle updatedVehicle =
                vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.InService);

        assertThat(updatedVehicle).isEqualTo(vehicle);
        verify(vehicleRepository).save(vehicle);
        assertThat(vehicle.getStatus()).isEqualTo(Vehicle.Status.InService);
    }

    @Test
    @DisplayName("GIVEN a vehicle to change the status WHEN transitioning to InMaintenance THEN status is updated")
    void testChangeStatusToInMaintenance() {
        vehicle.setVehicleInService();
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle updatedVehicle =
                vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.InMaintenance);

        assertThat(updatedVehicle).isEqualTo(vehicle);
        verify(vehicleRepository).save(vehicle);
        assertThat(vehicle.getStatus()).isEqualTo(Vehicle.Status.InMaintenance);
    }

    @Test
    @DisplayName("GIVEN a vehicle to change the status WHEN transitioning to OutOfService THEN status is updated")
    void testChangeStatusToOutOfService() {
        vehicle.setVehicleInService();
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(vehicle)).thenReturn(vehicle);

        Vehicle updatedVehicle =
                vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.OutOfService);

        assertThat(updatedVehicle).isEqualTo(vehicle);
        verify(vehicleRepository).save(vehicle);
        assertThat(vehicle.getStatus()).isEqualTo(Vehicle.Status.OutOfService);
    }

    @Test
    @DisplayName("GIVEN a vehicle to change the status WHEN it does not exist THEN an exception is thrown")
    void testChangeStatusVehicleDoesNotExist() {
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(
                () -> vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.OutOfService));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Vehicle", vehicle.getPlateNumber());
    }

    @Test
    @DisplayName("GIVEN a vehicle to change the status WHEN it has already that status THEN an exception is thrown")
    void testChangeStatusVehicleAlreadyWithThatStatus() {
        vehicle.setVehicleOutOfService();
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));

        Throwable throwable = catchThrowable(
                () -> vehicleManagementUseCase.changeStatus(vehicle.getPlateNumber(), Vehicle.Status.OutOfService));

        assertThat(throwable)
                .isInstanceOf(EntityStatusException.class)
                .hasMessageContainingAll("Vehicle", vehicle.getPlateNumber(), Vehicle.Status.OutOfService.toString());

        verify(vehicleRepository, never()).save(any(Vehicle.class));
    }
}
