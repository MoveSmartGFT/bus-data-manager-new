package com.movesmart.busdatamanager.vehicle.application.vehicleHistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.movesmart.busdatamanager.core.exception.EntityAlreadyExistsException;
import com.movesmart.busdatamanager.vehicle.VehicleInstancioModels;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistoryRepository;
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
public class CreateVehicleHistoryManagementUseCaseImplTest {

    @Mock
    private VehicleHistoryRepository vehicleHistoryRepository;

    @InjectMocks
    private VehicleHistoryManagementUseCaseImpl vehicleHistoryManagementUseCaseImpl;

    private final VehicleHistory vehicleHistory = Instancio.create(VehicleInstancioModels.VEHICLE_HISTORY_MODEL);

    @Test
    @DisplayName("GIVEN a vehicleHistory to create THEN returns vehicleHistory object")
    void testVehicleHistoryCreate() {
        when(vehicleHistoryRepository.findById(vehicleHistory.getId())).thenReturn(Optional.empty());
        when(vehicleHistoryRepository.save(any())).thenReturn(vehicleHistory);

        VehicleHistory vehicleHistoryCreated = vehicleHistoryManagementUseCaseImpl.create(vehicleHistory);

        assertThat(vehicleHistoryCreated).isEqualTo(vehicleHistory);
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
}
