package com.moveSmart.busDataManager.route.domain.route;

import com.moveSmart.busDataManager.core.infrastructure.persistence.InsertableRepository;
import lombok.Generated;
import org.springframework.data.repository.ListCrudRepository;

@Generated
public interface RouteRepository
        extends ListCrudRepository<Route, String>, InsertableRepository<Route> {
}
