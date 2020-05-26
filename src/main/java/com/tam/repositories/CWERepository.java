package com.tam.repositories;

import com.tam.model.MitreCWE.CWEItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CWERepository extends MongoRepository<CWEItem, String> {
}
