package com.tam.utils;

import com.tam.model.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CVEUtil {
    public static List<String> extractCPEsFromAnalysisDFDModel(AnalysisDFDModelResource dfdModelResource) {
        List<String> interactorCPEs = dfdModelResource.getInteractors()
                .stream()
                .filter(interactor -> interactor.getCpe() != null)
                .map(AnalysisInteractorResource::getCpe).collect(Collectors.toList());
        List<String> processCPEs = dfdModelResource.getProcesses()
                .stream()
                .filter(interactor -> interactor.getCpe() != null)
                .map(AnalysisProcessResource::getCpe).collect(Collectors.toList());
        List<String> dataStoreCPEs = dfdModelResource.getDataStores()
                .stream()
                .filter(interactor -> interactor.getCpe() != null)
                .map(AnalysisDataStoreResource::getCpe).collect(Collectors.toList());
        List<String> dataFlowCPEs = dfdModelResource.getDataFlows()
                .stream()
                .filter(interactor -> interactor.getCpe() != null)
                .map(AnalysisDataFlowResource::getCpe).collect(Collectors.toList());
        return Stream.of(interactorCPEs, processCPEs, dataStoreCPEs, dataFlowCPEs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
