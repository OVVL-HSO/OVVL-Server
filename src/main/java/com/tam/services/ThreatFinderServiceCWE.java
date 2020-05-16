package com.tam.services;

import com.tam.model.AnalysisDFDModelResource;
import com.tam.model.CWEThreatResource;
import com.tam.model.MitreCWE.ThreatCatalogue.AttributeSelection.*;
import com.tam.model.ThreatMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreatFinderServiceCWE {

    @Autowired
    public ThreatFinderServiceCWE() {
    }

    void findThreats(ThreatMetaData threatMetaData,
                     AnalysisDFDModelResource dfdModelToBeAnalyzed,
                     List<CWEThreatResource> foundThreats){
        CWEThreatTableSelectionAttributes elementAttributes = getCWEThreatTableSelectionAttributes();



    }

    private CWEThreatTableSelectionAttributes getCWEThreatTableSelectionAttributes(){
        // initialize the element attributes with the default case of unknown attributes
        CWEThreatTableSelectionAttributes elementAttributes = new CWEThreatTableSelectionAttributes(
                CWESelectionAccessVector.UNKNOWN,
                CWESelectionAuthentication.UNKNOWN,
                CWESelectionLanguage.UNKNOWN,
                CWESelectionTechnology.UNKNOWN
        ).builder().build();



        return elementAttributes;
    }

}
