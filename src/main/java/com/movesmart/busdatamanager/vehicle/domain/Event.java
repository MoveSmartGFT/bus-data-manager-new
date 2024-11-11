package com.movesmart.busdatamanager.vehicle.domain;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Generated
public class Event {
    @NotNull
    private LocalDateTime time;

    @NotBlank
    String details;


    @NotNull
    private boolean maintenance;

    @NotBlank
    private String notification;
}