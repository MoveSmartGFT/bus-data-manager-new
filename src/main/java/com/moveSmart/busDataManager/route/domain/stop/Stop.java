package com.moveSmart.busDataManager.route.domain.stop;

import com.moveSmart.busDataManager.route.domain.Coordinates;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Identity;

@Getter
@Entity
@RequiredArgsConstructor
@Generated
@Table(name = "STOP")
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
}