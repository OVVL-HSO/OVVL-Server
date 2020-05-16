package com.tam.utils;

import com.tam.model.AppliedStrideThreatResource;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteUtil {
    static <T> List<T> deleteDuplicatesFromAList(List<T> listWithDuplicates) {
        return listWithDuplicates.stream().distinct().collect(Collectors.toList());
    }

    public static List<AppliedStrideThreatResource> deleteDuplicateSTRIDEThreatsFromThreatList(List<AppliedStrideThreatResource> newThreats,
                                                                                               List<AppliedStrideThreatResource> oldThreats) {
        List<List<String>> affectedElements = newThreats
                .stream()
                .map(AppliedStrideThreatResource::getAffectedElements)
                .collect(Collectors.toList());
        affectedElements.forEach(affectedElement -> oldThreats.removeIf(threat -> threat.getAffectedElements().containsAll(affectedElement)));
        newThreats.addAll(oldThreats);
        return newThreats;
    }

    static void removeThreatsWhichAffectCertainElements(List<AppliedStrideThreatResource> threats,
                                                                                      List<String> elementIDs) {
        elementIDs.forEach(elementID -> threats.removeIf(threat -> threat.getAffectedElements().contains(elementID)));
    }

    public static void deleteThreatFromThreatList(List<AppliedStrideThreatResource> applyingThreats, String threatID) {
        applyingThreats.removeIf(threat -> threat.getThreatID().equals(threatID));
    }
}
