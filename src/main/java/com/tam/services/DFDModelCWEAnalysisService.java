package com.tam.services;

import com.tam.model.AnalysisDFDModelResource;
import com.tam.model.AnalysisDataFlowResource;
import com.tam.model.CWEThreatResource;
import com.tam.model.ThreatMetaData;
import com.tam.utils.ThreatGenerationServiceHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DFDModelCWEAnalysisService {

    private ThreatFinderServiceCWE threatFinderServiceCWE;

    @Autowired
    public DFDModelCWEAnalysisService(ThreatFinderServiceCWE threatFinderServiceCWE) {
        System.out.println("DFDModelCWEAnalysisService Constructor");
        this.threatFinderServiceCWE = threatFinderServiceCWE;
    }

    public List<CWEThreatResource> analyzeCWEThreatModel(AnalysisDFDModelResource dfdModelToBeAnalyzed) {
        System.out.println("analyzeCWEThreatModel");
        return findCWEThreatsOnSelectedThreatModel(dfdModelToBeAnalyzed);
    }

    private List<CWEThreatResource> findCWEThreatsOnSelectedThreatModel(AnalysisDFDModelResource dfdModelToBeAnalyzed) {
        System.out.println("findCWEThreatsOnSelectedThreatModel");
        List<CWEThreatResource> foundThreats = new ArrayList<>();
        dfdModelToBeAnalyzed.getDataFlows()
                .forEach(dataFlow -> getCWEThreats(dataFlow, dfdModelToBeAnalyzed, foundThreats));
        return foundThreats;
    }


    private void getCWEThreats(AnalysisDataFlowResource dataFlow,
                               AnalysisDFDModelResource dfdModelToBeAnalyzed,
                               List<CWEThreatResource> foundThreats) {

        // This metadata is used to generate found threats
        // It is built from the start and end element of a dataflow
        // At first, the type is set very generic. It gets specified later.
        ThreatMetaData threatMetaData = ThreatGenerationServiceHelperUtil.getRequiredDataForThreatGenerationFromElements(dataFlow);

        threatFinderServiceCWE.findThreats(threatMetaData, dfdModelToBeAnalyzed ,dataFlow , foundThreats);



    }

    // Find the end element on a dataflow indepenent of its type
    private void findDataFlowEndElement(){

    }
}
