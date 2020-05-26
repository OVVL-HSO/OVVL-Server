package com.tam.converter;


import com.tam.model.CWEResource;
import com.tam.model.CommonConsequencesResource;
import com.tam.model.DetectionMethodsResource;
import com.tam.model.MitreCWE.CWECommonConsequence;
import com.tam.model.MitreCWE.CWEDetectionMethods;
import com.tam.model.MitreCWE.CWEItem;
import com.tam.model.MitreCWE.CWEPotentialMitigations;
import com.tam.model.PotentialMitigationsResource;

import java.util.List;
import java.util.stream.Collectors;

public class CWEItemToCWEResourceConverter {


    public static List<CWEResource> convertCWEItemsToCWEResources(List<CWEItem> cwes) {
        return cwes.stream().map(CWEItemToCWEResourceConverter::convert).collect(Collectors.toList());
    }

    public static CWEResource convertCWEItemToCWERessouce(CWEItem cwe){
        return convert(cwe);
    }

    private static CWEResource convert(CWEItem cwe) {
        return new CWEResource()
                .id(cwe.getId())
                .name(cwe.getCweName())
                .description(cwe.getDescription())
                .detectionMethods(convertDetectionMethods(cwe.getDetectionMethods()))
                .commonConsequences(convertCommonConsequences(cwe.getCommonConsequences()))
                .potentialMitigations(convertPotentialMitigations(cwe.getPotentialMitigations()));
    }

    private static List<DetectionMethodsResource> convertDetectionMethods(List<CWEDetectionMethods> cweDetectionMethods){
        return cweDetectionMethods.stream().map(CWEItemToCWEResourceConverter::convertDetectionMethod).collect(Collectors.toList());
    }

    private static DetectionMethodsResource convertDetectionMethod(CWEDetectionMethods cweDetectionMethod){
        return new DetectionMethodsResource()
                .description(cweDetectionMethod.getDescription())
                .effectiveness(cweDetectionMethod.getEffectiveness())
                .effectivenessNotes(cweDetectionMethod.getEffectivenessNotes())
                .method(cweDetectionMethod.getMethod());
    }

    private static List<CommonConsequencesResource> convertCommonConsequences(List<CWECommonConsequence> cweCommonConsequences){
        return cweCommonConsequences.stream().map(CWEItemToCWEResourceConverter::convertCommonConsequence).collect(Collectors.toList());
    }

    private static CommonConsequencesResource convertCommonConsequence(CWECommonConsequence cweCommonConsequence){
        return new CommonConsequencesResource()
                .impact(cweCommonConsequence.getImpact())
                .scope(cweCommonConsequence.getScope())
                .note(cweCommonConsequence.getNote());
    }

    private static List<PotentialMitigationsResource> convertPotentialMitigations(List<CWEPotentialMitigations> cwePotentialMitigations){
        return cwePotentialMitigations.stream().map(CWEItemToCWEResourceConverter::convertPotentialMitigation).collect(Collectors.toList());
    }

    private static PotentialMitigationsResource convertPotentialMitigation(CWEPotentialMitigations cwePotentialMitigation){
        return new PotentialMitigationsResource()
                .description(cwePotentialMitigation.getDescription())
                .phase(cwePotentialMitigation.getPhase())
                .strategy(cwePotentialMitigation.getStrategy());
    }

}
