package com.movesmart.busdatamanager.route.domain.stop;

import com.movesmart.busdatamanager.route.domain.Coordinates;
import jakarta.persistence.Embedded;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "stop")
@AggregateRoot
@RequiredArgsConstructor
@Generated
public class Stop {

    /**
     * Identifier for the stop
     */
    @Identity
    @Id
    @NotNull
    private String id;

    /**
     * Name of the stop
     */
    @NotBlank
    private String name;

    /**
     * Location of the stop
     */
    @Valid
    @NotNull
    @Embedded
    private Coordinates location;

    public Stop(String id, String name, Coordinates location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}