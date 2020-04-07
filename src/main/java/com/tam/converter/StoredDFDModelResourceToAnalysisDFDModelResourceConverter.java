package com.tam.converter;

import com.tam.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class StoredDFDModelResourceToAnalysisDFDModelResourceConverter {


    public static AnalysisDFDModelResource convert(StoredDFDModelResource storedDFDModel) {
        return convertStoredDFDModelToAnalysisDFDModel(storedDFDModel);
    }

    private static AnalysisDFDModelResource convertStoredDFDModelToAnalysisDFDModel(StoredDFDModelResource storedDFDModelResource){
        return new AnalysisDFDModelResource()
                .interactors(convertInteractorsToAnalysisInteractors(storedDFDModelResource.getInteractors()))
                .dataFlows(convertDataFlowsToAnalysisDataFlows(storedDFDModelResource.getDataFlows()))
                .dataStores(convertDataStoresToAnalysisDataStores(storedDFDModelResource.getDataStores()))
                .processes(convertProcessesToAnalysisProcesses(storedDFDModelResource.getProcesses()))
                .boundaries(convertTrustBoundariesToAnalyisBoundaries(storedDFDModelResource.getTrustBoundaries()))
                .modelID(storedDFDModelResource.getModelID() != null ? storedDFDModelResource.getModelID() : "");
    }

    // Convert Interactors:
    private static List<AnalysisInteractorResource> convertInteractorsToAnalysisInteractors(List<InteractorResource> interactorResource){
        return interactorResource.stream().map(StoredDFDModelResourceToAnalysisDFDModelResourceConverter::convertInteractorToAnalysisInteractor).collect(Collectors.toList());
    }

    private static AnalysisInteractorResource convertInteractorToAnalysisInteractor(InteractorResource interactorResource){
        return new AnalysisInteractorResource()
                .id(interactorResource.getId())
                .name(interactorResource.getName())
                .type(interactorResource.getType())
                .options(interactorResource.getOptions())
                .cpe(interactorResource.getCpe() != null ? interactorResource.getCpe().getCpe23Name() : "");
    }

    // Covert DataFlows
    private static List<AnalysisDataFlowResource> convertDataFlowsToAnalysisDataFlows(List<DataFlowResource> storedDataFlow){
        return storedDataFlow.stream().map(StoredDFDModelResourceToAnalysisDFDModelResourceConverter::convertDataFlowToAnalysisDataFlow).collect(Collectors.toList());
    }

    private static AnalysisDataFlowResource convertDataFlowToAnalysisDataFlow(DataFlowResource storedDataFlow){
        return new AnalysisDataFlowResource()
                .id(storedDataFlow.getId())
                .name(storedDataFlow.getName())
                .startElement(convertPositionedConnectedElementToConnectedElement(storedDataFlow.getConnectedElements().getStartElement()))
                .endElement(convertPositionedConnectedElementToConnectedElement(storedDataFlow.getConnectedElements().getEndElement()))
                .type(storedDataFlow.getType())
                .options(storedDataFlow.getOptions())
                .cpe(storedDataFlow.getCpe() != null ? storedDataFlow.getCpe().getCpe23Name() : "");
    }

    private static ConnectedElementResource convertPositionedConnectedElementToConnectedElement(PositionedConnectedElementResource positionedConnectedElementResource){
        return new ConnectedElementResource()
                .id(positionedConnectedElementResource.getId())
                .type(positionedConnectedElementResource.getType());
    }

    // Convert DataStores
    private static List<AnalysisDataStoreResource> convertDataStoresToAnalysisDataStores(List<DataStoreResource> storedDataStore){
        return storedDataStore.stream().map(StoredDFDModelResourceToAnalysisDFDModelResourceConverter::convertDataStoreToAnalysisDataStore).collect(Collectors.toList());
    }

    private static AnalysisDataStoreResource convertDataStoreToAnalysisDataStore(DataStoreResource storedDataStore){
        return new AnalysisDataStoreResource()
                .id(storedDataStore.getId())
                .name(storedDataStore.getName())
                .type(storedDataStore.getType())
                .options(storedDataStore.getOptions())
                .cpe(storedDataStore.getCpe() != null ? storedDataStore.getCpe().getCpe23Name() : "");
    }

    // Convert Processes
    private static List<AnalysisProcessResource> convertProcessesToAnalysisProcesses(List<ProcessResource> processResources){
        return processResources.stream().map(StoredDFDModelResourceToAnalysisDFDModelResourceConverter::convertProcessToAnalysisProcess).collect(Collectors.toList());
    }

    private static AnalysisProcessResource convertProcessToAnalysisProcess(ProcessResource processResource){
        return new AnalysisProcessResource()
                .id(processResource.getId())
                .name(processResource.getName())
                .type(processResource.getType())
                .options(processResource.getOptions())
                .cpe(processResource.getCpe() != null ? processResource.getCpe().getCpe23Name() : "");
    }

    // Convert Boundaries
    private static List<AnalysisBoundaryResource> convertTrustBoundariesToAnalyisBoundaries(List<TrustBoundaryResource> trustBoundaryResources){
        return trustBoundaryResources.stream().map(StoredDFDModelResourceToAnalysisDFDModelResourceConverter::convertTrustBoundaryToAnalyisBoundary).collect(Collectors.toList());
    }

    private static AnalysisBoundaryResource convertTrustBoundaryToAnalyisBoundary(TrustBoundaryResource trustBoundaryResource){
        return new AnalysisBoundaryResource()
                .id(trustBoundaryResource.getId())
                .name(trustBoundaryResource.getName())
                .type(trustBoundaryResource.getType())
                .options(trustBoundaryResource.getOptions());
    }


}
