package com.tam.services;

import com.tam.model.*;
import com.tam.model.MitreCWE.ThreatCatalogue.AttributeSelection.*;
import com.tam.utils.ElementTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ThreatFinderServiceCWE {

    @Autowired
    public ThreatFinderServiceCWE() {
    }

    void findThreats(ThreatMetaData threatMetaData,
                     AnalysisDFDModelResource dfdModelToBeAnalyzed,
                     AnalysisDataFlowResource dataFlow,
                     List<CWEThreatResource> foundThreats){
        CWEThreatTableSelectionAttributes elementAttributes = getCWEThreatTableSelectionAttributes(threatMetaData, dfdModelToBeAnalyzed);



    }

    private CWEThreatTableSelectionAttributes getCWEThreatTableSelectionAttributes(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed){
        // initialize the element attributes with the default case of unknown attributes
        CWEThreatTableSelectionAttributes elementAttributes = new CWEThreatTableSelectionAttributes(
                getCWESelectionAccessVector(threatMetaData, dfdModelToBeAnalyzed),
                getCWESelectionAuthentication(threatMetaData, dfdModelToBeAnalyzed),
                CWESelectionLanguage.UNKNOWN,
                CWESelectionTechnology.UNKNOWN
        );




        //ThreatGenerationServiceHelperUtil.createAffectedElementsList

        //return new CWEThreatResource()
        //        .
        /*
        *
        *  return new STRIDEThreatResource()
                .threatCategory(STRIDEResource.INFORMATION_DISCLOSURE)
                .title("Missing Encryption")
                .description("An attacker might be able to access sensitive data if they get access to " + name + ". Consider encrypting the data.")
                .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData));
        * */

        return elementAttributes;
    }

    // determine the access vector of connection/two elements by checking if they are in the same trust boundary
    private CWESelectionAccessVector getCWESelectionAccessVector(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed){
        String startElementId = threatMetaData.getStartElementID();
        String endElementId = threatMetaData.getEndElementID();
        List<String> elementIds = Arrays.asList(startElementId, endElementId);

        if (dfdModelToBeAnalyzed.getTrustBoundaries() == null) {
            return CWESelectionAccessVector.UNKNOWN;
        }

        if (dfdModelToBeAnalyzed.getTrustBoundaries().size()>0){
            for (AnalysisBoundaryResource boundary : dfdModelToBeAnalyzed.getTrustBoundaries()) {
                if(boundary.getElements().contains(startElementId) ||boundary.getElements().contains(endElementId)){
                    if (boundary.getElements().containsAll(elementIds)){
                        return  CWESelectionAccessVector.NETWORK;
                    }
                }
            }
            return CWESelectionAccessVector.LOCAL;
        }
        return CWESelectionAccessVector.UNKNOWN;
    }

    private CWESelectionAuthentication getCWESelectionAuthentication(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed){
        GenericSelectionResource startElementAuthenticatesItself = GenericSelectionResource.NOT_SELECTED;
        GenericSelectionResource endElementAuthenticatesItself = GenericSelectionResource.NOT_SELECTED;
        GenericSelectionResource startElementRequiresAuthentication = GenericSelectionResource.NOT_SELECTED;
        GenericSelectionResource endElementRequiresAuthentication = GenericSelectionResource.NOT_SELECTED;

        if (threatMetaData.getEndElementType() == "process"){
            AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
            if (process != null) {
                endElementRequiresAuthentication = process.getOptions().getRequiresAuthentication();
            }
        }

        switch (threatMetaData.getStartElementType()){
            case "process":
                // process.getOptions().getAuthenticatesItself();
                AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getStartElementID());
                if (process != null){
                    endElementRequiresAuthentication = process.getOptions().getAuthenticatesItself();
                }
                break;
            case "interactor":
                AnalysisInteractorResource interactor = ElementTypeUtil.findInteractor(dfdModelToBeAnalyzed.getInteractors(), threatMetaData.getStartElementID());
                if (interactor != null){
                    endElementRequiresAuthentication = interactor.getOptions().getAuthenticatesItself();
                }
                break;
        }

        if (endElementRequiresAuthentication == GenericSelectionResource.YES || startElementAuthenticatesItself == GenericSelectionResource.YES){
            return CWESelectionAuthentication.TRUE;
        } else if (endElementRequiresAuthentication == GenericSelectionResource.YES || startElementAuthenticatesItself == GenericSelectionResource.YES) {
            return CWESelectionAuthentication.FALSE;
        }

        return CWESelectionAuthentication.UNKNOWN;
    }



}
