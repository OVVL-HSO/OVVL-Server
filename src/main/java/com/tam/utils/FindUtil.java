package com.tam.utils;

import com.tam.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
public class FindUtil {
    public static <T> T findByProperty(List<T> collection, Predicate<T> filter) {
        return collection.stream().filter(filter).findFirst().orElse(null);
    }

    private static <T> List<T> findElementsConnectedToADataFlow(List<T> elements,
                                                                Predicate<T> filter) {
        return elements.stream().filter(filter).collect(Collectors.toList());
    }

    static List<AnalysisDataFlowResource> findDataFlowsConnectedToAnElement(List<AnalysisDataFlowResource> dataFlows,
                                                                            String elementID) {
        return dataFlows.stream().filter(dataFlow
                -> dataFlow.getStartElement().getId().equals(elementID)
                || dataFlow.getEndElement().getId().equals(elementID)).collect(Collectors.toList());
    }

    static void findInteractorsConnectedInAlistOfDataFlowsAndAddThemToDifferingElements(AnalysisDFDModelResource differingElements,
                                                                                        List<AnalysisDataFlowResource> connectedDataFlows,
                                                                                        AnalysisDFDModelResource newModel,
                                                                                        String updatedElementID) {
        List<AnalysisInteractorResource> connectedInteractors = FindUtil.
                findAllInteractorsConnectedInAListOfDataFlows(connectedDataFlows, newModel.getInteractors(), updatedElementID);
        if (!connectedInteractors.isEmpty()) {
            List<AnalysisInteractorResource> differingInteractors = differingElements.getInteractors();
            differingInteractors.addAll(connectedInteractors);
            differingElements.setInteractors(differingInteractors);
        }
    }

    static void findProcessesConnectedInAlistOfDataFlowsAddThemToDifferingElements(AnalysisDFDModelResource differingElements,
                                                                                   List<AnalysisDataFlowResource> connectedDataFlows,
                                                                                   AnalysisDFDModelResource newModel,
                                                                                   String updatedElementID) {
        List<AnalysisProcessResource> connectedProcesses = FindUtil.
                findAllProcessesConnectedInAListOfDataFlows(connectedDataFlows, newModel.getProcesses(), updatedElementID);
        if (!connectedProcesses.isEmpty()) {
            List<AnalysisProcessResource> differingProcesses = differingElements.getProcesses();
            differingProcesses.addAll(connectedProcesses);
            differingElements.setProcesses(differingProcesses);
        }
    }

    static void findDataStoresConnectedInAlistOfDataFlowsAndAddThemToDifferingElements(AnalysisDFDModelResource differingElements,
                                                                                       List<AnalysisDataFlowResource> connectedDataFlows,
                                                                                       AnalysisDFDModelResource newModel,
                                                                                       String updatedElementID) {
        List<AnalysisDataStoreResource> connectedDataStores = FindUtil.
                findAllDataStoresConnectedInAListOfDataFlows(connectedDataFlows, newModel.getDataStores(), updatedElementID);
        if (!connectedDataStores.isEmpty()) {
            List<AnalysisDataStoreResource> differingDataStores = differingElements.getDataStores();
            differingDataStores.addAll(connectedDataStores);
            differingElements.setDataStores(differingDataStores);
        }
    }

    private static List<AnalysisInteractorResource> findAllInteractorsConnectedInAListOfDataFlows
            (List<AnalysisDataFlowResource> connectedDataFlows,
             List<AnalysisInteractorResource> interactors,
             String updatedElementID) {
        List<AnalysisInteractorResource> allInteractorsConnectedToUpdatedDFs = new ArrayList<>();
        connectedDataFlows.forEach(dataflow -> {
            List<AnalysisInteractorResource> connectedInteractors
                    = FindUtil.findElementsConnectedToADataFlow(interactors,
                    FilterUtil.getInteractorIsConnectedToDataFlowFilter(dataflow, updatedElementID));
            if (!connectedInteractors.isEmpty()) {
                allInteractorsConnectedToUpdatedDFs.addAll(connectedInteractors);
            }
        });
        return allInteractorsConnectedToUpdatedDFs;
    }

    private static List<AnalysisProcessResource> findAllProcessesConnectedInAListOfDataFlows
            (List<AnalysisDataFlowResource> connectedDataFlows,
             List<AnalysisProcessResource> processes,
             String updatedElementID) {
        List<AnalysisProcessResource> allProcessesConnectedToUpdatedDFs = new ArrayList<>();
        connectedDataFlows.forEach(dataflow -> {
            List<AnalysisProcessResource> connectedProcesses
                    = FindUtil.findElementsConnectedToADataFlow(processes,
                    FilterUtil.getProcessIsConnectedToDataFlowFilter(dataflow, updatedElementID));
            if (!connectedProcesses.isEmpty()) {
                allProcessesConnectedToUpdatedDFs.addAll(connectedProcesses);
            }
        });
        return allProcessesConnectedToUpdatedDFs;
    }

    private static List<AnalysisDataStoreResource> findAllDataStoresConnectedInAListOfDataFlows
            (List<AnalysisDataFlowResource> connectedDataFlows,
             List<AnalysisDataStoreResource> dataStores,
             String updatedElementID) {
        List<AnalysisDataStoreResource> allDataStoresConnectedToUpdatedDFs = new ArrayList<>();
        connectedDataFlows.forEach(dataflow -> {
            List<AnalysisDataStoreResource> connectedDataStores
                    = FindUtil.findElementsConnectedToADataFlow(dataStores,
                    FilterUtil.getDataStoreIsConnectedToDataFlowFilter(dataflow, updatedElementID));
            if (!connectedDataStores.isEmpty()) {
                allDataStoresConnectedToUpdatedDFs.addAll(connectedDataStores);
            }
        });
        return allDataStoresConnectedToUpdatedDFs;
    }

    static List<String> findInteractorIDsNotInInteractorList(List<AnalysisInteractorResource> newInteractors,
                                                             List<AnalysisInteractorResource> oldInteractors) {
        oldInteractors.removeAll(newInteractors);
        return oldInteractors.stream().map(AnalysisInteractorResource::getId).collect(Collectors.toList());
    }

    static List<String> findProcessIDsNotInProcessList(List<AnalysisProcessResource> newProcesses,
                                                       List<AnalysisProcessResource> oldProcesses) {
        oldProcesses.removeAll(newProcesses);
        return oldProcesses.stream().map(AnalysisProcessResource::getId).collect(Collectors.toList());
    }

    static List<String> findDataStorIDsNotInDataStoreList(List<AnalysisDataStoreResource> newDataStores,
                                                          List<AnalysisDataStoreResource> oldDataStores) {
        oldDataStores.removeAll(newDataStores);
        return oldDataStores.stream().map(AnalysisDataStoreResource::getId).collect(Collectors.toList());
    }

    static List<String> findDataFlowIDSNotInDataFlowList(List<AnalysisDataFlowResource> newDataFlows,
                                                         List<AnalysisDataFlowResource> oldDataFlows) {
        oldDataFlows.removeAll(newDataFlows);
        return oldDataFlows.stream().map(AnalysisDataFlowResource::getId).collect(Collectors.toList());
    }

    public static boolean threatExistsInListOfThreats(List<AppliedStrideThreatResource> applyingThreats, String threatID) {
        return applyingThreats.stream().anyMatch(threat -> threat.getThreatID().equals(threatID));
    }

    public static ModelStorageData findStorageDataByID(String modelID, List<ModelStorageData> storageDataList) {
        return storageDataList.stream().filter(storageData -> storageData.getModelID().equals(modelID)).findFirst().orElse(null);

    }
}
