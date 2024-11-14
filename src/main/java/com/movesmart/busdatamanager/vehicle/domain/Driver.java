package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Generated
public class Driver {
    @NotNull
    String id;

    @NotBlank
    String name;

    @NotNull
    Integer contact;
}
