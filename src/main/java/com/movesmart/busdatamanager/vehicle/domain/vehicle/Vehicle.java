package com.movesmart.busdatamanager.vehicle.domain.vehicle;

import com.movesmart.busdatamanager.vehicle.domain.Coordinates;
import com.movesmart.busdatamanager.vehicle.domain.Event;
import com.movesmart.busdatamanager.vehicle.domain.VehicleHistory;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.annotation.ValueObject;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Vehicle Aggregate Root
 */
@Getter
@Document(collection = "vehicle")
@RequiredArgsConstructor
@AggregateRoot
@AllArgsConstructor
@Generated
public class Vehicle {

    /**
     * Identifier (plate number) for the vehicle
     */
    @Identity
    @Id
    @NotBlank
    private String plateNumber;

    /**
     * Capacity of the vehicle
     */
    @NotNull
    @Positive
    private Integer capacity;

    /**
     * Type of vehicle
     */
    @NotBlank
    private String type;

    /**
     * Coordinates of the vehicle
     */
    @NotNull
    @Valid
    private Coordinates location;

    /**
     * Events of the vehicle
     */
    @Valid
    @ElementCollection
    private List<Event> events;

    /**
     * Speed of the vehicle
     */
    @NotNull
    @Positive
    private Double speed;

    /**
     * Direction of the vehicle
     */
    @NotBlank
    private String direction;

    /**
     * Historical of the vehicle
     */
    @Valid
    @ElementCollection
    private List<VehicleHistory> vehicleHistory;

    /**
     * Status of the vehicle
     */
    @NotNull
    private Status status;

    /**
     * Creator of the vehicle
     */
    public Vehicle(
            String plateNumber,
            Integer capacity,
            String type,
            Coordinates location,
            List<Event> events,
            double speed,
            String direction,
            List<VehicleHistory> vehicleHistory) {
        this.plateNumber = plateNumber;
        this.capacity = capacity;
        this.status = Status.InService;
        this.type = type;
        this.location = location;
        this.events = events;
        this.speed = speed;
        this.direction = direction;
        this.vehicleHistory = vehicleHistory;
    }

    public Vehicle(String plateNumber, @Valid Status status) {
        this.plateNumber = plateNumber;
        this.status = status;
    }

    /**
     * Vehicle in service
     */
    public void setVehicleInService() {
        this.status = Status.InService;
    }

    /**
     * Vehicle out of service
     */
    public void setVehicleOutOfService() {
        this.status = Status.OutOfService;
    }

    /**
     * Vehicle in maintenance
     */
    public void setVehicleInMaintenance() {
        this.status = Status.InMaintenance;
    }

    /**
     * Possible status of the vehicle
     */
    @ValueObject
    public enum Status {
        InService,
        OutOfService,
        InMaintenance
    }
}
