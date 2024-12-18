package com.movesmart.busdatamanager.vehicle;

import static org.instancio.Select.field;

import com.movesmart.busdatamanager.vehicle.domain.Coordinates;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.domain.vehicleHistory.VehicleHistory;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.model.CoordinatesDTO;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.ChangeStatusVehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.UpdateVehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicleHistory.dto.VehicleHistoryRequest;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

@UtilityClass
public class VehicleInstancioModels {

    public static final Model<Vehicle> VEHICLE_MODEL = Instancio.of(Vehicle.class)
            .supply(
                    field(Vehicle::getPlateNumber),
                    () -> Instancio.gen().string().numericSequence().length(4).get()
                            + Instancio.gen().string().length(3))
            .supply(
                    field(Vehicle::getLocation),
                    () -> Coordinates.of(
                            Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                            Instancio.gen().doubles().min(-180.0).max(180.0).get()))
            .toModel();

    public static final Model<VehicleRequest> VEHICLE_REQUEST_MODEL = Instancio.of(VehicleRequest.class)
            .supply(
                    field(VehicleRequest::plateNumber),
                    () -> Instancio.gen().string().digits().length(4).get()
                            + Instancio.gen().string().length(3).get())
            .supply(
                    field(VehicleRequest::location),
                    () -> CoordinatesDTO.of(
                            Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                            Instancio.gen().doubles().min(-180.0).max(180.0).get()))
            .toModel();

    public static final Model<UpdateVehicleRequest> UPDATE_VEHICLE_REQUEST_MODEL = Instancio.of(
                    UpdateVehicleRequest.class)
            .supply(
                    field(UpdateVehicleRequest::plateNumber),
                    () -> Instancio.gen().string().digits().length(4).get()
                            + Instancio.gen().string().upperCase().length(3).get())
            .supply(
                    field(UpdateVehicleRequest::location),
                    () -> CoordinatesDTO.of(
                            Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                            Instancio.gen().doubles().min(-180.0).max(180.0).get()))
            .toModel();

    public static final Model<ChangeStatusVehicleRequest> CHANGE_STATUS_VEHICLE_REQUEST_MODEL = Instancio.of(
                    ChangeStatusVehicleRequest.class)
            .supply(
                    field(ChangeStatusVehicleRequest::plateNumber),
                    () -> Instancio.gen().string().digits().length(4).get()
                            + Instancio.gen().string().upperCase().length(3).get())
            .supply(
                    field(ChangeStatusVehicleRequest::status),
                    () -> Instancio.gen().enumOf(Vehicle.Status.class).get())
            .toModel();

    public static final Model<VehicleHistory> VEHICLE_HISTORY_MODEL =
            Instancio.of(VehicleHistory.class).toModel();

    public static final Model<VehicleHistoryRequest> VEHICLE_HISTORY_REQUEST_MODEL =
            Instancio.of(VehicleHistoryRequest.class).toModel();

    public static Model<VehicleHistoryRequest> getCreateVehicleHistoryRequestModelWithVehicle(String vehicleId) {
        return Instancio.of(VehicleHistoryRequest.class)
                .set(field(VehicleHistoryRequest::vehicleId), vehicleId)
                .toModel();
    }
}
