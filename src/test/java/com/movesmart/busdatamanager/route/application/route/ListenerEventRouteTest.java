package com.movesmart.busdatamanager.route.application.route;

import com.movesmart.busdatamanager.core.exception.EntityNotFoundException;
import com.movesmart.busdatamanager.core.infrastructure.api.RouteValidationEvent;
import com.movesmart.busdatamanager.route.RouteInstancioModels;
import com.movesmart.busdatamanager.route.domain.route.Route;
import com.movesmart.busdatamanager.route.domain.route.RouteRepository;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.junit.InstancioExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(InstancioExtension.class)
@ExtendWith(MockitoExtension.class)
public class ListenerEventRouteTest {

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private RouteEventListener routeEventListener;

    private RouteValidationEvent event;

    private final Route route = Instancio.create(RouteInstancioModels.ROUTE_MODEL);

    @BeforeEach
    public void setUp() {
        event = new RouteValidationEvent("route123", false);
    }

    @Test
    public void testRouteExistsAndIsEnabled() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findEnabledRouteById(route.getId())).thenReturn(Optional.of(route));

        RouteValidationEvent event = new RouteValidationEvent(route.getId(), false);

        routeEventListener.handleRouteValidationEvent(event);

        assertThat(event.isValidated()).isTrue();
    }

    @Test
    public void testNonExistingRouteToValidate() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.empty());

        RouteValidationEvent event = new RouteValidationEvent(route.getId(), false);

        routeEventListener.handleRouteValidationEvent(event);

        assertThat(event.isValidated()).isFalse();
    }

    @Test
    public void testRouteExistsButIsNotEnabled() {
        when(routeRepository.findById(route.getId())).thenReturn(Optional.of(route));
        when(routeRepository.findEnabledRouteById(route.getId())).thenReturn(Optional.empty());

        RouteValidationEvent event = new RouteValidationEvent(route.getId(), false);

        routeEventListener.handleRouteValidationEvent(event);

        assertThat(event.isValidated()).isFalse();
    }
}

