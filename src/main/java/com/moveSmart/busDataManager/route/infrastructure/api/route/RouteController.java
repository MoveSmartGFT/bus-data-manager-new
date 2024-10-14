package com.moveSmart.busDataManager.route.infrastructure.api.route;

import lombok.Generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RouteController.ROUTE_PATH)
@Slf4j
@RequiredArgsConstructor
@Generated //Eliminar cuando se vaya a implementar algun metodo
public class RouteController {
    public static final String ROUTE_PATH = "/api/v1/routes";
    public static final String ROUTE_ID_PATH = "/{routeId}";
}
