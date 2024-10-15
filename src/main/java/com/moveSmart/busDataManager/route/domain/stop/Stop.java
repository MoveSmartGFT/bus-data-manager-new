package com.moveSmart.busDataManager.route.domain.stop;

import com.moveSmart.busDataManager.route.domain.Coordinates;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Entity
@RequiredArgsConstructor
@Generated
@Table(name = "stop")
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
    private Coordinates location;
}