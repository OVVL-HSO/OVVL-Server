package com.tam.repositories;

import com.tam.model.SettingsResource;
import com.tam.model.StoredSettingsData;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface SettingsRepository extends MongoRepository<StoredSettingsData, String> {
    public Optional<StoredSettingsData> findSettingsByUsername(String username);
    public void deleteByUsername(String username);
}
