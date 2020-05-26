package com.tam.repositories;

import com.tam.model.MitreCWE.ThreatCatalogue.CWEThreatItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CWEThreatCatalogueRepository extends MongoRepository<CWEThreatItem,String> {

    CWEThreatItem findFirstByAccessVectorEqualsAndAuthenticationEqualsAndLanguageEqualsAndTechnologyEqualsAllIgnoreCase(String accessVector, String authentication, String language, String technology);
    List<CWEThreatItem> findAllByAccessVectorEqualsAllIgnoreCase(String accessVector);
}
