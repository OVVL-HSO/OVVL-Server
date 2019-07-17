package com.tam.repositories;

import com.tam.model.StoredDFDModelResource;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface DFDModelRepository extends MongoRepository<StoredDFDModelResource, String> {
    public Optional<StoredDFDModelResource> findByModelID(String username);
    public Optional<StoredDFDModelResource> deleteByModelID(String username);
}
