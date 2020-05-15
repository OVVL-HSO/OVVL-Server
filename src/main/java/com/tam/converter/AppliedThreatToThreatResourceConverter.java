package com.tam.converter;

import com.tam.model.AppliedSTRIDEThreatResource;
import com.tam.model.StrideThreatResource;

public class AppliedThreatToThreatResourceConverter {
    public static StrideThreatResource convert(AppliedSTRIDEThreatResource threatResource) {
        return new StrideThreatResource()
                .title(threatResource.getTitle())
                .threatCategory(threatResource.getThreatCategory())
                .description(threatResource.getDescription())
                .affectedElements(threatResource.getAffectedElements());
    }
}
