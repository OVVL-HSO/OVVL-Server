package com.tam.services;

import com.tam.converter.CWEItemToCWEResourceConverter;
import com.tam.model.*;
import com.tam.model.MitreCWE.CWEItem;
import com.tam.model.MitreCWE.ThreatCatalogue.AttributeSelection.*;
import com.tam.model.MitreCWE.ThreatCatalogue.CWEPotentialWeakness;
import com.tam.model.MitreCWE.ThreatCatalogue.CWEThreatItem;
import com.tam.repositories.CWERepository;
import com.tam.repositories.CWEThreatCatalogueRepository;
import com.tam.utils.ElementTypeUtil;
import com.tam.utils.ThreatGenerationServiceHelperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreatFinderServiceCWE {

    CWEThreatCatalogueRepository cweThreatCatalogueRepository;
    CWERepository cweRepository;
    Float minWeaknessPropotionInSelectionThreshold;


    @Autowired
    public ThreatFinderServiceCWE(CWEThreatCatalogueRepository cweThreatCatalogueRepository, CWERepository cweRepository) {
        this.cweThreatCatalogueRepository = cweThreatCatalogueRepository;
        this.cweRepository = cweRepository;
        this.minWeaknessPropotionInSelectionThreshold = 0.02f;
    }

    void findThreats(ThreatMetaData threatMetaData,
                     AnalysisDFDModelResource dfdModelToBeAnalyzed,
                     AnalysisDataFlowResource dataFlow,
                     List<CWEThreatResource> foundThreats){
        getCWEThreatTableSelectionAttributes(threatMetaData, dfdModelToBeAnalyzed, foundThreats);

    }

    private void getCWEThreatTableSelectionAttributes(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed, List<CWEThreatResource> foundThreats){
        // initialize the element attributes with the default case of unknown attributes
        CWEThreatTableSelectionAttributes elementAttributes = new CWEThreatTableSelectionAttributes(
                getCWESelectionAccessVector(threatMetaData, dfdModelToBeAnalyzed),
                getCWESelectionAuthentication(threatMetaData, dfdModelToBeAnalyzed),
                getCWESelectionLanguage(threatMetaData, dfdModelToBeAnalyzed),
                getCWESelectionTechnology(threatMetaData, dfdModelToBeAnalyzed)
        );

        // If all attributes of an anlysed model are unspecified, the analysis process is skipped, as the analysis is not substantive
        if (elementAttributes.getAccessVector() == CWESelectionAccessVector.UNKNOWN &&
            elementAttributes.getAuthentication() == CWESelectionAuthentication.UNKNOWN &&
            elementAttributes.getLanguage() == CWESelectionLanguage.UNKNOWN &&
            elementAttributes.getTechnology() == CWESelectionTechnology.UNKNOWN){
            return;
        }

        // Filter uncommon/unexploitable DFD communication patterns
        if (threatMetaData.getEndElementType().equals("interactor")){
            return;
        } else if (threatMetaData.getEndElementType().equals("process") && threatMetaData.getStartElementType().equals("datastore")){
            return;
        }

        List<CWEItem> cwes = cweRepository.findAll();
        CWEThreatItem cweThreatItem = cweThreatCatalogueRepository.findFirstByAccessVectorEqualsAndAuthenticationEqualsAndLanguageEqualsAndTechnologyEqualsAllIgnoreCase(elementAttributes.getAccessVector().toString(), elementAttributes.getAuthentication().toString(), elementAttributes.getLanguage().toString(),elementAttributes.getTechnology().toString());

        for (CWEPotentialWeakness weakness : cweThreatItem.getPotentialWeaknesses()){

            if(weakness.getCwePercentageInSelection() < minWeaknessPropotionInSelectionThreshold){
                continue;
            }

            CWEItem cweItem = null;

            if(weakness.getCweID() != 0){
                List<CWEItem> cweItems = cwes.stream().filter(cweItem1 -> cweItem1.getId() == weakness.getCweID()).collect(Collectors.toList());
                if (cweItems.size() >0){
                    cweItem = cweItems.get(0);
                }
            } else {
                // Handle special case of text-classified Weakness, which is not part of the official MITRE CWE List:
                cweItem = CWEItem.builder()
                        .id(0)
                        .cweName("Unspecified Weakness")
                        .description("For the selection, a large number of weaknesses were found which cannot be assigned to a specific CWE. A classification process was used to identify an assessment of potential threats according to the STRIDE methodology.")
                        .detectionMethods(new ArrayList<>())
                        .commonConsequences(new ArrayList<>())
                        .potentialMitigations(new ArrayList<>())
                        .build();
            }

            if (cweItem != null){
                CWEResource cweResource = CWEItemToCWEResourceConverter.convertCWEItemToCWERessouce(cweItem);

             cweResource.setAnalysisInformation(new CweThreatAnalysisInformationResource()
                     .spoofingProportion(BigDecimal.valueOf(weakness.getSpoofingProportion()))
                     .tamperingProportion(BigDecimal.valueOf(weakness.getTamperingProportion()))
                     .informationDisclosureProportion(BigDecimal.valueOf(weakness.getInformationDisclosureProportion()))
                     .denialOfServiceProportion(BigDecimal.valueOf(weakness.getDenialOfServiceProportion()))
                     .elevationOfPrivilegeProportion(BigDecimal.valueOf(weakness.getElevationOfPrivilegeProportion()))
                     .cwePercentageInSelection(BigDecimal.valueOf(weakness.getCwePercentageInSelection()))
                     .meanBaseScore(BigDecimal.valueOf(weakness.getMeanBaseScore()))
                     .meanExploitabilityScore(BigDecimal.valueOf(weakness.getMeanExploitabilityScore()))
                     .meanImpactScore(BigDecimal.valueOf(weakness.getMeanImpactScore()))
             );

                CWEThreatResource cweThreatResource = new CWEThreatResource()
                        .cwe(cweResource)
                        .affectedElements(ThreatGenerationServiceHelperUtil.createAffectedElementsList(threatMetaData)
                        );
                foundThreats.add(cweThreatResource);
            }
        }
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

        if (threatMetaData.getEndElementType().equals("process")){
            AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
            if (process != null) {
                endElementRequiresAuthentication = process.getOptions().getRequiresAuthentication();
            }
        }

        switch (threatMetaData.getStartElementType()){
            case "process":
                AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getStartElementID());
                if (process != null){
                    startElementAuthenticatesItself = process.getOptions().getAuthenticatesItself();
                }
                break;
            case "interactor":
                AnalysisInteractorResource interactor = ElementTypeUtil.findInteractor(dfdModelToBeAnalyzed.getInteractors(), threatMetaData.getStartElementID());
                if (interactor != null){
                    startElementAuthenticatesItself = interactor.getOptions().getAuthenticatesItself();
                }
                break;
        }

        if (endElementRequiresAuthentication == GenericSelectionResource.YES || startElementAuthenticatesItself == GenericSelectionResource.YES){
            return CWESelectionAuthentication.TRUE;
        } else if (endElementRequiresAuthentication == GenericSelectionResource.NO || startElementAuthenticatesItself == GenericSelectionResource.NO) {
            return CWESelectionAuthentication.FALSE;
        }

        return CWESelectionAuthentication.UNKNOWN;
    }

    private CWESelectionTechnology getCWESelectionTechnology(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed){
        CWESelectionTechnology endElementTechnology = CWESelectionTechnology.UNKNOWN;

        switch (threatMetaData.getEndElementType()){
            case "process":
                AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
                if (process != null){
                    if(process.getType() == ProcessTypeResource.WEB_SERVER || process.getType() == ProcessTypeResource.WEB_SERVICE){
                        endElementTechnology = CWESelectionTechnology.WEB;
                    }
                }
                break;
            case "datastore":
                AnalysisDataStoreResource dataStore = ElementTypeUtil.findDataStore(dfdModelToBeAnalyzed.getDataStores(), threatMetaData.getEndElementID());
                if (dataStore != null){
                     if(dataStore.getType() == DataStoreTypeResource.SQL_DATABASE || dataStore.getType() == DataStoreTypeResource.NOSQL_DATABASE){
                         endElementTechnology = CWESelectionTechnology.DATABASE;
                     }
                }
                break;
        }
        return endElementTechnology;
    }

    private CWESelectionLanguage getCWESelectionLanguage(ThreatMetaData threatMetaData, AnalysisDFDModelResource dfdModelToBeAnalyzed){
        CWESelectionLanguage endElementLanguage = CWESelectionLanguage.UNKNOWN;

        if (threatMetaData.getEndElementType() == "process"){
            AnalysisProcessResource process = ElementTypeUtil.findProcess(dfdModelToBeAnalyzed.getProcesses(), threatMetaData.getEndElementID());
            switch(process.getOptions().getProgrammingLanguage()){
                case WEB:
                    endElementLanguage = CWESelectionLanguage.WEB;
                    break;
                case COMPILED:
                    endElementLanguage = CWESelectionLanguage.COMPILED;
                    break;
                case INTERPRETED:
                    endElementLanguage = CWESelectionLanguage.INTERPRETED;
                    break;
                case JIT_COMPILED:
                    endElementLanguage = CWESelectionLanguage.JITCOMPILED;
                    break;
            }
        }
        return endElementLanguage;
    }


}
