package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.route.domain.schedule.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Route Aggregate Root
 */
@Getter
@Document(collection = "route")
@RequiredArgsConstructor
@AllArgsConstructor
@Generated
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
     * List of stopIds for the route
     */
    private List<String> stopIds;

    /**
     * Schedules of the route
     */
    @NotNull
    @ElementCollection
    private List<Schedule> schedules;
}


