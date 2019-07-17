package com.tam.utils;

import com.tam.model.*;

import java.util.List;

// Utility functions relating to the element type, which mainly help the ThreatModelAnalysisService.
public class ElementTypeUtil {

    public static AnalysisInteractorResource findInteractor(List<AnalysisInteractorResource> interactors, String elementID) {
        return interactors.stream()
                .filter(interactor -> interactor.getId().equals(elementID))
                .findAny()
                .orElse(null);
    }

    public static AnalysisProcessResource findProcess(List<AnalysisProcessResource> processes, String elementID) {
        return processes.stream()
                .filter(process -> process.getId().equals(elementID))
                .findAny()
                .orElse(null);
    }

    public static AnalysisDataStoreResource findDataStore(List<AnalysisDataStoreResource> dataStores, String elementID) {
        return dataStores.stream()
                .filter(dataStore -> dataStore.getId().equals(elementID))
                .findAny()
                .orElse(null);
    }

    public static boolean interactorStartElementAndProcessEndElement(ThreatMetaData threatMetaData) {
        return (threatMetaData.getStartElementType().equals("interactor")
                && threatMetaData.getEndElementType().equals("process"));
    }

    public static boolean processStartElementAndProcessEndElement(ThreatMetaData threatMetaData) {
        return (threatMetaData.getStartElementType().equals("process")
                && threatMetaData.getEndElementType().equals("process"));
    }

    public static boolean dataStoreStartElementAndProcessEndElement(ThreatMetaData threatMetaData) {
        return (threatMetaData.getStartElementType().equals("datastore")
                && threatMetaData.getEndElementType().equals("process"));
    }

    public static boolean processStartElementAndDataStoreEndElement(ThreatMetaData threatMetaData) {
        return (threatMetaData.getStartElementType().equals("process")
                && threatMetaData.getEndElementType().equals("datastore"));
    }
}
