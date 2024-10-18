package com.moveSmart.busDataManager.route;

import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.List;

@UtilityClass
public class RouteInstancioModels {

    public static final Model<Stop> STOP_MODEL =
            Instancio.of(Stop.class)
            .toModel();

    public static final Model<Route> ROUTE_MODEL =
            Instancio.of(Route.class)
                    .toModel();

    public static final Model<List<Stop>> STOP_LIST_MODEL =
            Instancio.ofList(Stop.class)
                    .size(30)
                    .toModel();
}
