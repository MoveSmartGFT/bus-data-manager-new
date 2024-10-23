package com.moveSmart.busDataManager.route;

import com.moveSmart.busDataManager.route.domain.Coordinates;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import com.moveSmart.busDataManager.route.infrastructure.api.stop.dto.UpdateStopRequest;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.List;
import java.util.stream.Collectors;

import static org.instancio.Select.field;

@UtilityClass
public class RouteInstancioModels {

    public static final Model<Stop> STOP_MODEL =
            Instancio.of(Stop.class)
                    .supply(field(Stop::getLocation),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();

    public static final Model<UpdateStopRequest> UPDATE_STOP_REQUEST_MODEL =
            Instancio.of(UpdateStopRequest.class)
                    .supply(field(UpdateStopRequest::location),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();

    public static Model<Route> ROUTE_MODEL (List<Stop> stops) {
        List<String> stopIds = stops.stream().map(Stop::getId).collect(Collectors.toList());
        return Instancio.of(Route.class)
                .set(field(Route::getStopIds), stopIds)
                .toModel();
    }

    public static final Model<List<Stop>> STOP_LIST_MODEL =
            Instancio.ofList(Stop.class)
                    .size(30)
                    .supply(field(Stop::getLocation),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();
}
