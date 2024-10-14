package com.moveSmart.busDataManager.route.domain.stop;

import com.moveSmart.busDataManager.core.infrastructure.persistence.InsertableRepository;
import lombok.Generated;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Generated
@Repository
public interface StopRepository
        extends ListCrudRepository<Stop, String>, InsertableRepository<Stop> {
}
