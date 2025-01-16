package com.movesmart.busdatamanager.route.application.route;

import com.movesmart.busdatamanager.core.infrastructure.api.RouteValidationEvent;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouteEventListener {

    private final RouteRepository routeRepository;

    @EventListener
    public void handleRouteValidationEvent(RouteValidationEvent event) {
        log.info("Validating route with id {}", event.getRouteId());

        var routeExists = routeRepository.findById(event.getRouteId());

        if (routeExists.isEmpty()) {
            event.setValidated(false);
        }

        boolean isEnabled = routeRepository.findEnabledRouteById(event.getRouteId()).isPresent();
        event.setValidated(isEnabled);
    }
}
