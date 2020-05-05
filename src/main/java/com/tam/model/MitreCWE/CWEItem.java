package com.tam.model.MitreCWE;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;

@Getter
@Setter
@Builder
public class CWEItem {
    @Id
    private int id;
    private String cweName;
    private String weaknessAbstraction;
    private String status;
    private String description;
    private List<CWEExtendedDescription> extendedDescription;
    private List<CWERelatedWeakness> relatedWeaknesses;
    private List<CWEWeaknessOrdinalities> weaknessOrdinalities;
    private List<CWEApplicablePlatforms> applicablePlatforms;
    private String backgroundDetails;
    //private String alternateTerms;
    private List<CWEModeOfIntroduction> modeOfIntroduction;
    private String likelihoodOfExploitation;
    private List<CWECommonConsequence> commonConsequences;
    private List<CWEDetectionMethods> detectionMethods;
    private List<CWEPotentialMitigations> potentialMitigations;
    //private String observedExamples;
    //private String functionalAreas;
    private List<String> affectedResources;
    //private String taxonomyMappings;
    private List<Integer> relatedAttackPatterns;
    //private String notes;
}
