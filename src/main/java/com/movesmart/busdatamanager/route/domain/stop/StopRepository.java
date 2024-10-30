package com.movesmart.busdatamanager.route.domain.stop;

import lombok.Generated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Generated
@Repository
public interface StopRepository extends JpaRepository<Stop, String> {
}
