package com.tam.utils;

import com.tam.model.*;

import java.util.ArrayList;
import java.util.List;

public class AnalysisUtil {

    // FIXME: Lost of duplicate code here
    public static AnalysisDFDModelResource collectElementsContainedInDFDModelWhichNeedAnalysis
            (AnalysisDFDModelResource newModel, AnalysisDFDModelResource oldModel) {
        AnalysisDFDModelResource differingElements = new AnalysisDFDModelResource()
                .interactors(new ArrayList<>())
                .processes(new ArrayList<>())
                .dataStores(new ArrayList<>())
                .dataFlows(new ArrayList<>());
        newModel.getInteractors().forEach(updatedInteractor -> {
            // We look for the id of the "new" interactor in the existing interactors
            AnalysisInteractorResource existingInteractor =
                    FindUtil.findByProperty(oldModel.getInteractors(), oldModelInteractor
                            -> oldModelInteractor.getId().equals(updatedInteractor.getId()));
            addInteractorsWhichNeedUpdatingToNewModel(
                    existingInteractor,
                    updatedInteractor,
                    differingElements,
                    newModel);
        });
        newModel.getProcesses().forEach(updatedProcess -> {
            AnalysisProcessResource existingProcess =
                    FindUtil.findByProperty(oldModel.getProcesses(), oldModelProcess
                            -> oldModelProcess.getId().equals(updatedProcess.getId()));
            addProcessesWhichNeedUpdatingToNewModel(
                    existingProcess,
                    updatedProcess,
                    differingElements,
                    newModel);
        });
        newModel.getDataStores().forEach(updatedDatStore -> {
            AnalysisDataStoreResource existingDataStore =
                    FindUtil.findByProperty(oldModel.getDataStores(), oldModelDataStore
                            -> oldModelDataStore.getId().equals(updatedDatStore.getId()));
            addDataStoresWhichNeedUpdatingToNewModel(
                    existingDataStore,
                    updatedDatStore,
                    differingElements,
                    newModel);
        });
        newModel.getDataFlows().forEach(updatedDataFlow -> {
            AnalysisDataFlowResource existingDataFlow =
                    FindUtil.findByProperty(oldModel.getDataFlows(), oldModelDataFlow
                            -> oldModelDataFlow.getId().equals(updatedDataFlow.getId()));
            addDataFlowsWhichNeedUpdatingToNewModel(
                    existingDataFlow,
                    updatedDataFlow,
                    differingElements,
                    newModel);
        });
        return removeDuplicatesFromADFDModel(differingElements);
    }



    public static void checkIfElementsWereRemovedAndRemoveApplyingThreats(AnalysisDFDModelResource newModel,
                                                                   AnalysisDFDModelResource oldModel,
                                                                   List<AppliedSTRIDEThreatResource> foundThreats) {
        List<String> idsOfDeleteElements = new ArrayList<>();
        if (CheckUtil.interactorsWereRemoved(newModel.getInteractors(), oldModel.getInteractors())) {
            idsOfDeleteElements.addAll(FindUtil
                    .findInteractorIDsNotInInteractorList(newModel.getInteractors(), oldModel.getInteractors()));
        }
        if (CheckUtil.processesWereRemoved(newModel.getProcesses(), oldModel.getProcesses())) {
            idsOfDeleteElements.addAll(FindUtil
                    .findProcessIDsNotInProcessList(newModel.getProcesses(), oldModel.getProcesses()));
        }
        if (CheckUtil.dataStoresWereRemoved(newModel.getDataStores(), oldModel.getDataStores())) {
            idsOfDeleteElements.addAll(FindUtil
                    .findDataStorIDsNotInDataStoreList(newModel.getDataStores(), oldModel.getDataStores()));
        }
        if (CheckUtil.dataFlowsWereRemoved(newModel.getDataFlows(), oldModel.getDataFlows())) {
            idsOfDeleteElements.addAll(FindUtil
                    .findDataFlowIDSNotInDataFlowList(newModel.getDataFlows(), oldModel.getDataFlows()));
        }
        DeleteUtil.removeThreatsWhichAffectCertainElements(foundThreats, idsOfDeleteElements);
    }

    private static AnalysisDFDModelResource removeDuplicatesFromADFDModel(AnalysisDFDModelResource differingElements) {
        return new AnalysisDFDModelResource()
                .interactors(DeleteUtil.deleteDuplicatesFromAList(differingElements.getInteractors()))
                .processes(DeleteUtil.deleteDuplicatesFromAList(differingElements.getProcesses()))
                .dataStores(DeleteUtil.deleteDuplicatesFromAList(differingElements.getDataStores()))
                .dataFlows(DeleteUtil.deleteDuplicatesFromAList(differingElements.getDataFlows()));
    }

    private static void addInteractorsWhichNeedUpdatingToNewModel(AnalysisInteractorResource existingInteractor,
                                                                  AnalysisInteractorResource updatedInteractor,
                                                                  AnalysisDFDModelResource differingElements,
                                                                  AnalysisDFDModelResource newModel) {
        // If an interactor with the same ID exists, we need to check if the updated one different to the original
        if (existingInteractor == null || CheckUtil.twoInteractorsAreDifferent(updatedInteractor, existingInteractor)) {
            // If it's an update, we need to run a new analysis on it
            List<AnalysisInteractorResource> differingInteractors = differingElements.getInteractors();
            differingInteractors.add(updatedInteractor);
            differingElements.setInteractors(differingInteractors);
            // Now we added the updatedInteractor to the model which will be analyzed...
            // ...but we also need its connected DataFlows, and connected Elements
            addAllElementsConnectedToAnUpdatedElementToTheNewAnalysisModel(differingElements, updatedInteractor.getId(), newModel);
        }
    }

    private static void addProcessesWhichNeedUpdatingToNewModel(AnalysisProcessResource existingProcess,
                                                                AnalysisProcessResource updatedProcess,
                                                                AnalysisDFDModelResource differingElements,
                                                                AnalysisDFDModelResource newModel) {
        if (existingProcess == null || CheckUtil.twoPrcessesAreDifferent(updatedProcess, existingProcess)) {
            List<AnalysisProcessResource> differingProcesses = differingElements.getProcesses();
            differingProcesses.add(updatedProcess);
            differingElements.setProcesses(differingProcesses);
            addAllElementsConnectedToAnUpdatedElementToTheNewAnalysisModel(differingElements, updatedProcess.getId(), newModel);
        }
    }

    private static void addDataStoresWhichNeedUpdatingToNewModel(AnalysisDataStoreResource existingDataStore,
                                                                 AnalysisDataStoreResource updatedDatStore,
                                                                 AnalysisDFDModelResource differingElements,
                                                                 AnalysisDFDModelResource newModel) {
        if (existingDataStore == null || CheckUtil.twoDataStoresAreDifferent(updatedDatStore, existingDataStore)) {
                List<AnalysisDataStoreResource> differingDataStores = differingElements.getDataStores();
                differingDataStores.add(updatedDatStore);
                differingElements.setDataStores(differingDataStores);
                addAllElementsConnectedToAnUpdatedElementToTheNewAnalysisModel(differingElements, updatedDatStore.getId(), newModel);
        }
    }

    private static void addDataFlowsWhichNeedUpdatingToNewModel(AnalysisDataFlowResource existingDataFlow,
                                                                AnalysisDataFlowResource updatedDataFlow,
                                                                AnalysisDFDModelResource differingElements,
                                                                AnalysisDFDModelResource newModel) {
        if (existingDataFlow == null || CheckUtil.twoDataFlowsAreDifferent(updatedDataFlow, existingDataFlow)) {
            List<AnalysisDataFlowResource> differingDataFlows = differingElements.getDataFlows();
            differingDataFlows.add(updatedDataFlow);
            differingElements.setDataFlows(differingDataFlows);
            List<AnalysisDataFlowResource> listContainingOnlyTheUpdatedDataFlow = new ArrayList<>();
            listContainingOnlyTheUpdatedDataFlow.add(updatedDataFlow);
            // We can use the same method as used for the other elements, by adding a dataflow to a list and not specifiying an ID
            // This is really hacky, but works
            FindUtil.findInteractorsConnectedInAlistOfDataFlowsAndAddThemToDifferingElements
                    (differingElements,listContainingOnlyTheUpdatedDataFlow, newModel, "noIDneeded");
            FindUtil.findProcessesConnectedInAlistOfDataFlowsAddThemToDifferingElements
                    (differingElements,listContainingOnlyTheUpdatedDataFlow, newModel, "noIDneeded");
            FindUtil.findDataStoresConnectedInAlistOfDataFlowsAndAddThemToDifferingElements
                    (differingElements,listContainingOnlyTheUpdatedDataFlow, newModel, "noIDneeded");
        }
    }

    private static void addAllElementsConnectedToAnUpdatedElementToTheNewAnalysisModel(AnalysisDFDModelResource differingElements,
                                                                                       String updatedElementID,
                                                                                       AnalysisDFDModelResource newModel) {
        List<AnalysisDataFlowResource> connectedDataFlows =
                FindUtil.findDataFlowsConnectedToAnElement(newModel.getDataFlows(), updatedElementID);

        if (!connectedDataFlows.isEmpty()) {
            // Add the connected dataflows to the model
            List<AnalysisDataFlowResource> differingDataFlows = differingElements.getDataFlows();
            differingDataFlows.addAll(connectedDataFlows);
            differingElements.setDataFlows(differingDataFlows);

            // Add all other connected Elements to the model
            FindUtil.findInteractorsConnectedInAlistOfDataFlowsAndAddThemToDifferingElements
                    (differingElements, connectedDataFlows, newModel, updatedElementID);
            FindUtil.findProcessesConnectedInAlistOfDataFlowsAddThemToDifferingElements
                    (differingElements, connectedDataFlows, newModel, updatedElementID);
            FindUtil.findDataStoresConnectedInAlistOfDataFlowsAndAddThemToDifferingElements
                    (differingElements, connectedDataFlows, newModel, updatedElementID);
        }
    }
}
