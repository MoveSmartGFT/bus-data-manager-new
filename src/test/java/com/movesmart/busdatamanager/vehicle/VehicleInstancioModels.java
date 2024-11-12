package com.movesmart.busdatamanager.vehicle;

import com.movesmart.busdatamanager.vehicle.domain.Coordinates;
import com.movesmart.busdatamanager.vehicle.domain.vehicle.Vehicle;
import com.movesmart.busdatamanager.vehicle.infrastructure.api.vehicle.dto.VehicleRequest;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

import static org.instancio.Select.field;

@UtilityClass
public class VehicleInstancioModels {

    public static final Model<Vehicle> VEHICLE_MODEL =
            Instancio.of(Vehicle.class)
                    .supply(field(Vehicle::getPlateNumber),
                            () -> Instancio.gen().string().numericSequence().length(4).get()
                                    + Instancio.gen().string().length(3)
                    )
                    .supply(field(Vehicle::getLocation),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();

    public static final Model<VehicleRequest> VEHICLE_REQUEST_MODEL =
            Instancio.of(VehicleRequest.class)
                    .supply(field(VehicleRequest::plateNumber),
                            () -> Instancio.gen().string().digits().length(4).get()
                                    + Instancio.gen().string().length(3).get()
                    )
                    .supply(field(VehicleRequest::location),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();
}
