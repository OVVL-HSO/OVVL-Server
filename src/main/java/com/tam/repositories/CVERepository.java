package com.tam.repositories;

import com.tam.model.CVEResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CVERepository extends MongoRepository<CVEResource, String> {
}
