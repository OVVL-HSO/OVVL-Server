package com.tam.utils;

import com.tam.model.AnalysisDataFlowResource;
import com.tam.model.AnalysisDataStoreResource;
import com.tam.model.AnalysisInteractorResource;
import com.tam.model.AnalysisProcessResource;

import java.util.function.Predicate;

class FilterUtil {
    static Predicate<AnalysisInteractorResource> getInteractorIsConnectedToDataFlowFilter(AnalysisDataFlowResource dataflow,
                                                                                          String updatedElementID) {
        return  interactor -> (interactor.getId().equals(dataflow.getStartElement().getId())
                || interactor.getId().equals(dataflow.getEndElement().getId()))
                && !interactor.getId().equals(updatedElementID);
    }

    static Predicate<AnalysisProcessResource> getProcessIsConnectedToDataFlowFilter(AnalysisDataFlowResource dataflow,
                                                                                    String updatedElementID) {
        return  process -> (process.getId().equals(dataflow.getStartElement().getId())
                || process.getId().equals(dataflow.getEndElement().getId()))
                && !process.getId().equals(updatedElementID);
    }

    static Predicate<AnalysisDataStoreResource> getDataStoreIsConnectedToDataFlowFilter(AnalysisDataFlowResource dataflow,
                                                                                        String updatedElementID) {
        return  dataSTore -> (dataSTore.getId().equals(dataflow.getStartElement().getId())
                || dataSTore.getId().equals(dataflow.getEndElement().getId()))
                && !dataSTore.getId().equals(updatedElementID);
    }
}
