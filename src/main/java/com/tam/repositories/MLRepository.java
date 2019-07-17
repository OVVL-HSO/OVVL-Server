package com.tam.repositories;

import com.tam.model.ThreatDataSetResource;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface MLRepository extends MongoRepository<ThreatDataSetResource, String> { }

