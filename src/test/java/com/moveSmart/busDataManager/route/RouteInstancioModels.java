package com.moveSmart.busDataManager.route;

import com.moveSmart.busDataManager.route.domain.Coordinates;
import com.moveSmart.busDataManager.route.domain.route.Route;
import com.moveSmart.busDataManager.route.domain.stop.Stop;
import lombok.experimental.UtilityClass;
import org.instancio.Instancio;
import org.instancio.Model;

import java.util.concurrent.ThreadLocalRandom;

import static org.instancio.Select.field;

@UtilityClass
public class RouteInstancioModels {

    public static final Model<Stop> STOP_MODEL = Instancio.of(Stop.class)
            .supply(field(Coordinates.class, "latitude"), () -> ThreadLocalRandom.current().nextDouble(-90.0, 90.0))
            .supply(field(Coordinates.class, "longitude"), () -> ThreadLocalRandom.current().nextDouble(-180.0, 180.0))
            .toModel();

    public static final Model<Route> ROUTE_MODEL =
            Instancio.of(Route.class)
                    .toModel();

}

