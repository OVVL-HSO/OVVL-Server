package com.tam.utils;

import com.tam.model.*;

import java.util.List;

public class MLUtil {

    public static Object findMLElementItem(ConnectedElementResource elementData, AnalysisDFDModelResource model) {
        if (elementData.getType() == ElementTypeResource.INTERACTOR) {
            return findInteractorAndCreateMLElementItem(model.getInteractors(), elementData.getId());
        } else if (elementData.getType() == ElementTypeResource.PROCESS) {
            return findProcessAndCreateMLElementItem(model.getProcesses(), elementData.getId());
        } else {
            return findDataStoreAndCreateMLElementItem(model.getDataStores(), elementData.getId());
        }
    }

    public static Object createMLElementItemFromDataFlow(AnalysisDataFlowResource dataFlow) {
        return new MLElementItem(ElementTypeResource.DATAFLOW, dataFlow.getType(), dataFlow.getOptions());
    }

    private static Object findInteractorAndCreateMLElementItem(List<AnalysisInteractorResource> interactors, String elementID) {
        AnalysisInteractorResource foundInteractor
                = FindUtil.findByProperty(interactors, interactor -> interactor.getId().equals(elementID));
        return new MLElementItem(ElementTypeResource.INTERACTOR, foundInteractor.getType(), foundInteractor.getOptions());
    }

    private static Object findProcessAndCreateMLElementItem(List<AnalysisProcessResource> processes, String elementID) {
        AnalysisProcessResource foundProcess
                = FindUtil.findByProperty(processes, process -> process.getId().equals(elementID));
        return new MLElementItem(ElementTypeResource.PROCESS, foundProcess.getType(), foundProcess.getOptions());
    }

    private static Object findDataStoreAndCreateMLElementItem(List<AnalysisDataStoreResource> dataStores, String elementID) {
        AnalysisDataStoreResource foundDataStore
                = FindUtil.findByProperty(dataStores, dataStore -> dataStore.getId().equals(elementID));
        return new MLElementItem(ElementTypeResource.DATASTORE, foundDataStore.getType(), foundDataStore.getOptions());
    }
}
