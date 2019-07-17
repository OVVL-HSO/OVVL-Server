package com.tam.repositories;

import com.tam.model.ModelStorageData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface StorageDataRepository extends MongoRepository<ModelStorageData, String> {
    public Optional<List<ModelStorageData>> findAllByUsername(String username);
    public Optional<ModelStorageData> findByModelID(String modelID);
    public Optional<List<ModelStorageData>> findAllByModelID(String modelID);
    public Optional<List<ModelStorageData>> deleteAllByModelID(String modelID);
    public Optional<ModelStorageData> deleteByModelID(String modelID);
}
