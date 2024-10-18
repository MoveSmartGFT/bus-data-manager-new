package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.route.domain.stop.Stop;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import java.util.List;

/**
 * Route Aggregate Root
 */
@Getter
@AggregateRoot
@RequiredArgsConstructor
@Generated
@Entity
@Table(name = "ROUTE")
public class Route {

    /**
     * Identifier for the route
     */
    @Identity
    @Id
    @NotNull
    private String id;

    /**
     * Name of the route
     */
    @NotBlank
    private String name;

    /**
     * List of stops for the route
     */
//    @OneToMany
//    private List<Stop> stops;
    private String stopId;

    /**
     * Schedules of the route
     */
    private List<String> schedules;
}
