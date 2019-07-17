package com.tam.converter;

import com.tam.model.AppliedThreatResource;
import com.tam.model.ThreatResource;

public class AppliedThreatToThreatResourceConverter {
    public static ThreatResource convert(AppliedThreatResource threatResource) {
        return new ThreatResource()
                .title(threatResource.getTitle())
                .strideCategory(threatResource.getStrideCategory())
                .description(threatResource.getDescription())
                .affectedElements(threatResource.getAffectedElements());
    }
}
