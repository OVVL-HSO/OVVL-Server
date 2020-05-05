package com.tam.repositories;

import com.tam.model.MitreCWE.ThreatCatalogue.CWEThreatItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CWEThreatCatalogueRepository extends MongoRepository<CWEThreatItem,String> {

}
