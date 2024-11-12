package com.movesmart.busdatamanager.vehicle.application.vehicle;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.VehicleRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class CreateVehicleManagementUseCaseImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleManagementUseCaseImpl vehicleManagementUseCase;

    private final Vehicle vehicle = Instancio.create(VehicleInstancioModels.VEHICLE_MODEL);

    @Test
    @DisplayName("GIVEN a vehicle to create THEN returns vehicle object and status 201")
    void testVehicleCreate() {
        when(vehicleRepository.existsById(vehicle.getPlateNumber())).thenReturn(false);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        Vehicle vehicleCreated = vehicleManagementUseCase.create(vehicle);

        assertThat(vehicleCreated).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("GIVEN a vehicle to create WHEN already exists THEN returns an exception and status 409")
    void testVehicleCreateAlreadyExists() {
        when(vehicleRepository.existsById(vehicle.getPlateNumber())).thenReturn(true);

        Throwable throwable = catchThrowable(() -> vehicleManagementUseCase.create(vehicle));

        assertThat(throwable)
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContainingAll("Vehicle", vehicle.getPlateNumber());
    }
}
