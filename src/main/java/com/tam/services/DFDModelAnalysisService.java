package com.tam.services;

import com.tam.model.*;
import com.tam.utils.ElementTypeUtil;
import com.tam.utils.ThreatGenerationServiceHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DFDModelAnalysisService {

    private ThreatFinderService threatFinderService;

    @Autowired
    public DFDModelAnalysisService(ThreatFinderService threatFinderService) {
        this.threatFinderService = threatFinderService;
    }

    public List<ThreatResource> analyzeThreatModel(AnalysisDFDModelResource dfdModelToBeAnalyzed) {
        return findThreatsOnSelectedThreatModel(dfdModelToBeAnalyzed);
    }

    private List<ThreatResource> findThreatsOnSelectedThreatModel(AnalysisDFDModelResource dfdModelToBeAnalyzed) {
        List<ThreatResource> foundThreats = new ArrayList<>();
        dfdModelToBeAnalyzed.getDataFlows()
                .forEach(dataFlow -> getThreats(dataFlow, dfdModelToBeAnalyzed, foundThreats));
        return foundThreats;
    }

    private void getThreats(AnalysisDataFlowResource dataFlow,
                            AnalysisDFDModelResource dfdModelToBeAnalyzed,
                            List<ThreatResource> foundThreats) {
        // This metadata is used to generate found threats
        // It is built from the start and end element of a dataflow
        // At first, the type is set very generic. It gets specified later.
        ThreatMetaData threatMetaData = ThreatGenerationServiceHelperUtil
                .getRequiredDataForThreatGenerationFromElements(dataFlow);

        threatFinderService.findDataFlowThreats(foundThreats, dataFlow, threatMetaData);

        // The threat analysis basically works like this:
        // 1. Find the connection type (interactor -> process, process -> dataStore, etc.)
        // 2. Find the elements linked in the threatMetaData so their options can be checked later
        // 3. Find the threats for the current connection type in a separate method


        // TODO: General Hints -> Your System Ships with a default admin password and doesnt force a change
        // Interactor -> Process
        if (ElementTypeUtil.interactorStartElementAndProcessEndElement(threatMetaData)) {
            handleAnalysisForInteractorStartProcessEnd(dataFlow, dfdModelToBeAnalyzed, foundThreats, threatMetaData);
        }
        // Process -> Process
        else if (ElementTypeUtil.processStartElementAndProcessEndElement(threatMetaData)) {
            handleAnalysisForProcessStartAndProcessEnd(dataFlow, dfdModelToBeAnalyzed, foundThreats, threatMetaData);
        }
        // DataStore -> Process
        else if (ElementTypeUtil.dataStoreStartElementAndProcessEndElement(threatMetaData)) {
            handleAnalysisForCaseDataStoreStartAndProcessEnd(dataFlow, dfdModelToBeAnalyzed, foundThreats, threatMetaData);
        }
        // Process -> DataStore
        else if (ElementTypeUtil.processStartElementAndDataStoreEndElement(threatMetaData)) {
            handleAnalysisForProcessStartAndDataStoreEnd(dfdModelToBeAnalyzed, foundThreats, threatMetaData);
        }
    }

    private void handleAnalysisForProcessStartAndDataStoreEnd(AnalysisDFDModelResource dfdModelToBeAnalyzed,
                                                              List<ThreatResource> foundThreats,
                                                              ThreatMetaData threatMetaData) {
        AnalysisProcessResource startProcess = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getStartElementID());
        AnalysisDataStoreResource endDataStore = ElementTypeUtil.findDataStore(dfdModelToBeAnalyzed.getDataStores(), threatMetaData.getEndElementID());
        ThreatMetaData updatedThreatMetaData = ThreatGenerationServiceHelperUtil
                .updateThreatMetaData(threatMetaData, startProcess.getType().toString(), endDataStore.getType().toString());
        threatFinderService.findThreatsForProcessStartAndDataStoreEnd(updatedThreatMetaData, endDataStore, foundThreats);
    }

    private void handleAnalysisForCaseDataStoreStartAndProcessEnd(AnalysisDataFlowResource dataFlow,
                                                                  AnalysisDFDModelResource dfdModelToBeAnalyzed,
                                                                  List<ThreatResource> foundThreats,
                                                                  ThreatMetaData threatMetaData) {
        AnalysisDataStoreResource startDataStore = ElementTypeUtil.findDataStore(dfdModelToBeAnalyzed.getDataStores(), threatMetaData.getStartElementID());
        AnalysisProcessResource endProcess = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
        // Since we now know the exact element type, we can update the metadata
        ThreatMetaData updatedThreatMetaData = ThreatGenerationServiceHelperUtil
                .updateThreatMetaData(threatMetaData, startDataStore.getType().toString(), endProcess.getType().toString());
        threatFinderService.findThreatsForDataStoreStartAndProcessEnd(updatedThreatMetaData, startDataStore, endProcess, dataFlow, foundThreats);
    }

    private void handleAnalysisForProcessStartAndProcessEnd(AnalysisDataFlowResource dataFlow,
                                                            AnalysisDFDModelResource dfdModelToBeAnalyzed,
                                                            List<ThreatResource> foundThreats,
                                                            ThreatMetaData threatMetaData) {
        AnalysisProcessResource startProcess = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getStartElementID());
        AnalysisProcessResource endProcess = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
        // Since we now know the exact element type, we can update the metadata
        ThreatMetaData updatedThreatMetaData = ThreatGenerationServiceHelperUtil
                .updateThreatMetaData(threatMetaData, startProcess.getType().toString(), endProcess.getType().toString());
        threatFinderService.findThreatsForProcessStartAndProcessEnd(updatedThreatMetaData, startProcess, endProcess, dataFlow, foundThreats);
    }

    private void handleAnalysisForInteractorStartProcessEnd(AnalysisDataFlowResource dataFlow,
                                                            AnalysisDFDModelResource dfdModelToBeAnalyzed,
                                                            List<ThreatResource> foundThreats,
                                                            ThreatMetaData threatMetaData) {
        AnalysisInteractorResource startInteractor = ElementTypeUtil.findInteractor(dfdModelToBeAnalyzed.getInteractors(), threatMetaData.getStartElementID());
        AnalysisProcessResource endProcess = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
        // Since we now know the exact element type, we can update the metadata
        ThreatMetaData updatedThreatMetaData = ThreatGenerationServiceHelperUtil
                .updateThreatMetaData(threatMetaData, startInteractor.getType().toString(), endProcess.getType().toString());
        threatFinderService.findThreatsForInteractorStartAndProcessEnd(
                updatedThreatMetaData,
                startInteractor,
                endProcess,
                dataFlow,
                foundThreats);
    }
}
