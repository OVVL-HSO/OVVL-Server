package com.tam.converter;

import com.tam.model.ApplicableStateResource;
import com.tam.model.AppliedStrideThreatResource;
import com.tam.model.StrideThreatResource;
import com.tam.model.ThreatPriorityResource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class STRIDEThreatResourceToAppliedSTRIDEThreatConverter {

    public static List<AppliedStrideThreatResource> convertThreatResourcesToAppliedThreats(List<StrideThreatResource> threatResources) {
        return threatResources.stream().map(STRIDEThreatResourceToAppliedSTRIDEThreatConverter::convert).collect(Collectors.toList());
    }

    private static AppliedStrideThreatResource convert(StrideThreatResource threatResource) {
        return new AppliedStrideThreatResource()
                .threatID(UUID.randomUUID().toString())
                .title(threatResource.getTitle())
                .threatCategory(threatResource.getThreatCategory())
                .description(threatResource.getDescription())
                .applicable(ApplicableStateResource.NOT_SELECTED)
                .priority(ThreatPriorityResource.UNSPECIFIED)
                .affectedElements(threatResource.getAffectedElements());
    }
}
