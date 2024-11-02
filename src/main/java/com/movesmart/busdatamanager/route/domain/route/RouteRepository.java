package com.movesmart.busdatamanager.route.domain.route;

import lombok.Generated;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Generated
@Repository
public interface RouteRepository
        extends MongoRepository<Route, String> {
}
