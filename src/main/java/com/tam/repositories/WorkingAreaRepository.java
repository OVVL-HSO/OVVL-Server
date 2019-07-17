package com.tam.repositories;

import com.tam.model.WorkingArea;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface WorkingAreaRepository extends MongoRepository<WorkingArea, String> {
    Optional<WorkingArea> findFirstByUsername(String username);
    Long deleteAllByUsername(String username);
}
