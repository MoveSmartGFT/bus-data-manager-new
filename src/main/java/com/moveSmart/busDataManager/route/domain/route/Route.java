package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import lombok.Generated;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

/**
 * Route Aggregate Root
 */
@Generated
@Getter
@AggregateRoot
@Table(name = "route")
public class Route {

    /**
     * Identifier for the route
     */
    @Identity
    private String id;

    /**
     * Name of the route
     */
    private String name;

    /**
     * List of stops for the route
     */
    private List<Stop> stops;

    /**
     * Schedules of the route
     */
    private List<String> schedules;
}
