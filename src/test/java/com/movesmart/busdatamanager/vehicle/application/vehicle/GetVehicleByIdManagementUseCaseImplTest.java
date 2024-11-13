package com.movesmart.busdatamanager.vehicle.application.vehicle;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(InstancioExtension.class)
public class GetVehicleByIdManagementUseCaseImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleManagementUseCaseImpl vehicleManagementUseCase;

    private final Vehicle vehicle = Instancio.create(VehicleInstancioModels.VEHICLE_MODEL);

    @Test
    @DisplayName("GIVEN we try to get a vehicle WHEN it exist THEN a vehicle is received")
    void testGetVehicle() {
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.of(vehicle));

        Vehicle vehicleRetrieved = vehicleManagementUseCase.get(vehicle.getPlateNumber());

        assertThat(vehicleRetrieved).isEqualTo(vehicle);
    }

    @Test
    @DisplayName("GIVEN we try to retrieve a vehicle WHEN it does not exist THEN an exception is thrown")
    void testGetVehicleDoesNotExist() {
        when(vehicleRepository.findById(vehicle.getPlateNumber())).thenReturn(Optional.empty());

        Throwable throwable = catchThrowable(() -> vehicleManagementUseCase.get(vehicle.getPlateNumber()));

        assertThat(throwable)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContainingAll("Vehicle", vehicle.getPlateNumber());
    }
}
