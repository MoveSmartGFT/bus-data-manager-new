package com.movesmart.busdatamanager.core.infrastructure.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;

@Generated
@Data
@AllArgsConstructor
public class RouteValidationEvent {
    private final String routeId;
    private boolean validated;
}
