package com.tam.converter;

import com.tam.model.BaseDFDModelResource;
import com.tam.model.StoredDFDModelResource;

import java.util.UUID;

public class DFDModelResourceToEntityConverter {

    public static StoredDFDModelResource convert(BaseDFDModelResource dfdModel) {
        return new StoredDFDModelResource()
                .modelID(UUID.randomUUID().toString())
                .interactors(dfdModel.getInteractors())
                .processes(dfdModel.getProcesses())
                .dataStores(dfdModel.getDataStores())
                .dataFlows(dfdModel.getDataFlows())
                .trustBoundaries(dfdModel.getTrustBoundaries());
    }
}
