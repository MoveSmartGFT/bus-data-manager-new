package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Generated
public class Driver {
    @NotBlank
    String id;

    @NotBlank
    String name;

    @NotNull
    Integer contact;
}