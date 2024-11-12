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
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
     * Status of the vehicle
     */
    @NotBlank
    private String status;

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
     * Creator of the vehicle
     */
    public Vehicle(String plateNumber, Integer capacity, String status, String type, Coordinates location,
                   List<Event> events, double speed, String direction, List<VehicleHistory> vehicleHistory) {
        this.plateNumber = plateNumber;
        this.capacity = capacity;
        this.status = status;
        this.type = type;
        this.location = location;
        this.events = events;
        this.speed = speed;
        this.direction = direction;
        this.vehicleHistory = vehicleHistory;
    }
}