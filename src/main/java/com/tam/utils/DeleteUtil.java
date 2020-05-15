package com.tam.utils;

import com.tam.model.AppliedSTRIDEThreatResource;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteUtil {
    static <T> List<T> deleteDuplicatesFromAList(List<T> listWithDuplicates) {
        return listWithDuplicates.stream().distinct().collect(Collectors.toList());
    }

    public static List<AppliedSTRIDEThreatResource> deleteDuplicateSTRIDEThreatsFromThreatList(List<AppliedSTRIDEThreatResource> newThreats,
                                                                                               List<AppliedSTRIDEThreatResource> oldThreats) {
        List<List<String>> affectedElements = newThreats
                .stream()
                .map(AppliedSTRIDEThreatResource::getAffectedElements)
                .collect(Collectors.toList());
        affectedElements.forEach(affectedElement -> oldThreats.removeIf(threat -> threat.getAffectedElements().containsAll(affectedElement)));
        newThreats.addAll(oldThreats);
        return newThreats;
    }

    static void removeThreatsWhichAffectCertainElements(List<AppliedSTRIDEThreatResource> threats,
                                                                                      List<String> elementIDs) {
        elementIDs.forEach(elementID -> threats.removeIf(threat -> threat.getAffectedElements().contains(elementID)));
    }

    public static void deleteThreatFromThreatList(List<AppliedSTRIDEThreatResource> applyingThreats, String threatID) {
        applyingThreats.removeIf(threat -> threat.getThreatID().equals(threatID));
    }
}
