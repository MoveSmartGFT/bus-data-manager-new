package com.movesmart.busdatamanager.route.domain.route;

import com.movesmart.busdatamanager.route.domain.Schedule;
import io.hypersistence.tsid.TSID;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.*;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Route Aggregate Root
 */
@Getter
@Document(collection = "route")
@AggregateRoot
@AllArgsConstructor
@RequiredArgsConstructor
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
    @Valid
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
    public Route(String name, List<String> stopIds, List<Schedule> schedules) {
        this.id = TSID.Factory.getTsid().toString();
        this.name = name;
        this.stopIds = stopIds;
        this.schedules = schedules;
        this.status = Status.Enabled;
    }

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

    /**
     * Update the stopId list of the route
     */
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
