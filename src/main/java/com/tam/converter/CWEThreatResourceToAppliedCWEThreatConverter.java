package com.tam.converter;

import com.tam.model.AppliedCweThreatResource;
import com.tam.model.CWEThreatResource;

import java.util.List;
import java.util.stream.Collectors;

public class CWEThreatResourceToAppliedCWEThreatConverter {
    public static List<AppliedCweThreatResource> convertThreatResourcesToAppliedThreats(List<CWEThreatResource> threatResources) {
        return threatResources.stream().map(CWEThreatResourceToAppliedCWEThreatConverter::convert).collect(Collectors.toList());
    }

    private static AppliedCweThreatResource convert(CWEThreatResource threatResource) {
        return new AppliedCweThreatResource();
                //.(UUID.randomUUID().toString());
                /*.title(threatResource.getTitle())
                .threatCategory(threatResource.getThreatCategory())
                .description(threatResource.getDescription())
                .applicable(ApplicableStateResource.NOT_SELECTED)
                .priority(ThreatPriorityResource.UNSPECIFIED)
                .affectedElements(threatResource.getAffectedElements());*/
    }


}
