package com.movesmart.busdatamanager.route.domain.route;

import com.movesmart.busdatamanager.route.domain.Schedule;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Route Aggregate Root
 */
@Getter
@Document(collection = "route")
@RequiredArgsConstructor
@Entity
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
    @ElementCollection
    private List<String> stopIds;

    /**
     * Schedules of the route
     */
    @NotNull
    @ElementCollection
    private List<Schedule> schedules;

    /**
     * Status of the route
     */
    @NotNull
    private Status status;

    /**
     * Creator of the route
     */
    public Route(String id, String name, List<String> stopIds, List<Schedule> schedules) {
        this.id = id;
        this.name = name;
        this.stopIds = stopIds;
        this.schedules = schedules;
        this.status = Status.Enabled;
    }

    /**
     * Disables the route
     */
    public void disable() {
        this.status = Status.Disabled;
    }

    /**
     * Enabled the route
     */
    public void enable() {
        this.status = Status.Enabled;
    }

    public void updateStopIdList(List<String> stopIdsList) {
        this.stopIds = stopIdsList;
    }

    /**
     * Possible status of the route
     */
    @ValueObject
    public enum Status {
        Enabled,
        Disabled
    }
}


