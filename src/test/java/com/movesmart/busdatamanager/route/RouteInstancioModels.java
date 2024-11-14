package com.movesmart.busdatamanager.route;

import com.movesmart.busdatamanager.route.domain.Coordinates;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.stop.Stop;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.CreateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.route.dto.UpdateRouteRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.UpdateRouteStopsRequest;
import com.movesmart.busdatamanager.route.infrastructure.api.stop.dto.StopRequest;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.List;

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

    public static final Model<StopRequest> STOP_REQUEST_MODEL =
            Instancio.of(StopRequest.class)
                    .supply(field(StopRequest::location),
                            () -> Coordinates.of(
                                    Instancio.gen().doubles().min(-90.0).max(90.0).get(),
                                    Instancio.gen().doubles().min(-180.0).max(180.0).get()
                            ))
                    .toModel();

    public static final Model<Route> ROUTE_MODEL = Instancio.of(Route.class)
                .toModel();

    public static final Model<CreateRouteRequest> CREATE_ROUTE_REQUEST_MODEL = Instancio.of(CreateRouteRequest.class)
            .toModel();

    public static final Model<UpdateRouteRequest> UPDATE_ROUTE_REQUEST_MODEL = Instancio.of(UpdateRouteRequest.class)
            .toModel();

    public static final Model<UpdateRouteStopsRequest> UPDATE_ROUTE_STOPS_REQUEST_MODEL = Instancio.of(UpdateRouteStopsRequest.class)
            .toModel();

    public static Model<CreateRouteRequest> getCreateRouteRequestModelWithStops(List<String> stopIds) {
        return Instancio.of(CreateRouteRequest.class)
                .set(field(CreateRouteRequest::stopIds), stopIds)
                .toModel();
    }

    public static Model<Route> getRouteModelWithStops(List<String> stopIds) {
        return Instancio.of(Route.class)
                .set(field(Route::getStopIds), stopIds)
                .toModel();
    }

    public static Model<UpdateRouteRequest> getUpdateRouteRequestModelWithStops (List<String> stopIds) {
        return Instancio.of(UpdateRouteRequest.class)
                .set(field(UpdateRouteRequest::stopIds), stopIds)
                .toModel();
    }

    public static Model<UpdateRouteStopsRequest> getUpdateRouteStopsRequestModelWithStops(List<String> stopIds) {
        return Instancio.of(UpdateRouteStopsRequest.class)
                .set(field(UpdateRouteStopsRequest::stopIds), stopIds)
                .toModel();
    }

}
