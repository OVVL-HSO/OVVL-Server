package com.tam.converter;

import com.tam.model.AppliedSTRIDEThreatResource;
import com.tam.model.STRIDEThreatResource;

public class AppliedThreatToThreatResourceConverter {
    public static STRIDEThreatResource convert(AppliedSTRIDEThreatResource threatResource) {
        return new STRIDEThreatResource()
                .title(threatResource.getTitle())
                .threatCategory(threatResource.getThreatCategory())
                .description(threatResource.getDescription())
                .affectedElements(threatResource.getAffectedElements());
    }
}
