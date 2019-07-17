package com.tam.repositories;

import com.tam.model.CPEItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CPERepository extends MongoRepository<CPEItem, String> {
    @Query(value = "{'title': {$regex : ?0, $options: 'i'}}")
    public List<CPEItem> findAllByTitleMatchesRegex(String query);
}
