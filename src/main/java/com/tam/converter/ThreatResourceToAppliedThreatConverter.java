package com.tam.converter;

import com.tam.model.ApplicableStateResource;
import com.tam.model.AppliedThreatResource;
import com.tam.model.ThreatPriorityResource;
import com.tam.model.ThreatResource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ThreatResourceToAppliedThreatConverter {

    public static List<AppliedThreatResource> convertThreatResourcesToAppliedThreats(List<ThreatResource> threatResources) {
        return threatResources.stream().map(ThreatResourceToAppliedThreatConverter::convert).collect(Collectors.toList());
    }

    private static AppliedThreatResource convert(ThreatResource threatResource) {
        return new AppliedThreatResource()
                .threatID(UUID.randomUUID().toString())
                .title(threatResource.getTitle())
                .strideCategory(threatResource.getStrideCategory())
                .description(threatResource.getDescription())
                .applicable(ApplicableStateResource.NOT_SELECTED)
                .priority(ThreatPriorityResource.UNSPECIFIED)
                .affectedElements(threatResource.getAffectedElements());
    }
}
