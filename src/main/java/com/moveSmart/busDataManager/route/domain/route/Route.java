package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Generated;
import lombok.Getter;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.List;

/**
 * Route Aggregate Root
 */
@Generated
@Getter
@AggregateRoot
@Entity
@Table(name = "route")
public class Route {

    /**
     * Identifier for the route
     */
    @Identity
    @Id
    private String id;

    /**
     * Name of the route
     */
    private String name;

    /**
     * List of stops for the route
     */
    @OneToMany
    private List<Stop> stops;

    /**
     * Schedules of the route
     */
    private List<String> schedules;
}
