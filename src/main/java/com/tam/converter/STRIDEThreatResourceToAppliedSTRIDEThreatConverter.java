package com.tam.converter;

import com.tam.model.ApplicableStateResource;
import com.tam.model.AppliedSTRIDEThreatResource;
import com.tam.model.ThreatPriorityResource;
import com.tam.model.STRIDEThreatResource;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class STRIDEThreatResourceToAppliedSTRIDEThreatConverter {

    public static List<AppliedSTRIDEThreatResource> convertThreatResourcesToAppliedThreats(List<STRIDEThreatResource> threatResources) {
        return threatResources.stream().map(STRIDEThreatResourceToAppliedSTRIDEThreatConverter::convert).collect(Collectors.toList());
    }

    private static AppliedSTRIDEThreatResource convert(STRIDEThreatResource threatResource) {
        return new AppliedSTRIDEThreatResource()
                .threatID(UUID.randomUUID().toString())
                .title(threatResource.getTitle())
                .threatCategory(threatResource.getThreatCategory())
                .description(threatResource.getDescription())
                .applicable(ApplicableStateResource.NOT_SELECTED)
                .priority(ThreatPriorityResource.UNSPECIFIED)
                .affectedElements(threatResource.getAffectedElements());
    }
}
