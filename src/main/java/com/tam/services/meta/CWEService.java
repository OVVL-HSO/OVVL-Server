package com.tam.services.meta;

import com.tam.model.MitreCWE.*;
import com.tam.repositories.CWERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class CWEService {

    private CWERepository cweRepository;

    @Autowired
    public CWEService(CWERepository cweRepository) {
        this.cweRepository = cweRepository;
    }

    public void fillDBWithCWEData() throws IOException {
        try {
            List<String> cweFiles = getCWEFileList();

            for (String cweFile : cweFiles){
                File xmlFile = new File(cweFile);


                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);
                doc.getDocumentElement().normalize();


                NodeList nList = doc.getElementsByTagName("Weakness");
                for (int temp = 0; temp < nList.getLength(); temp++) {

                    Node nNode = nList.item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;

                        CWEItem cwe = CWEItem.builder()
                                .id(Integer.parseInt(eElement.getAttribute("ID")))
                                .cweName(eElement.getAttribute("Name"))
                                .weaknessAbstraction(eElement.getAttribute("weaknessAbstraction"))
                                .status(eElement.getAttribute("Status"))
                                .description(eElement.getElementsByTagName("Description").item(0).getTextContent())
                                .extendedDescription(getElementExtendedDescription(eElement))
                                .relatedWeaknesses(getElementRelatedWeaknesses(eElement))
                                .weaknessOrdinalities(getElementWeaknessOrdinalities(eElement))
                                .applicablePlatforms(getElementApplicablePlatforms(eElement))
                                .backgroundDetails((eElement.getElementsByTagName("Background_Details").getLength() > 0) ? eElement.getElementsByTagName("Background_Detail").item(0).getTextContent() : "")
                                .modeOfIntroduction(getElementModesOfIntroduction(eElement))
                                .likelihoodOfExploitation((eElement.getElementsByTagName("Likelihood_Of_Exploit").getLength() > 0) ? eElement.getElementsByTagName("Likelihood_Of_Exploit").item(0).getTextContent() : "unknown")
                                .commonConsequences(getElementCommonConsequences(eElement))
                                .detectionMethods(getElementDetectionMethods(eElement))
                                .potentialMitigations(getElementPotentialMitigations(eElement))
                                .affectedResources(getElementAffectedResources(eElement))
                                .relatedAttackPatterns(getElementRelatedAttackPatterns(eElement))
                                .build();

                        cweRepository.save(cwe);
                    }
                }
            }



        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private List<CWEExtendedDescription> getElementExtendedDescription(Element eElement){
        List<CWEExtendedDescription> extendedDescriptions = new ArrayList<>();
        try {
            NodeList extendedDescriptionNode = eElement.getElementsByTagName("Extended_Description");

            if (extendedDescriptionNode.getLength() > 0) {
                if (extendedDescriptionNode.getLength() == 1) {
                    CWEExtendedDescription extendedDescription = CWEExtendedDescription.builder()
                            .description(extendedDescriptionNode.item(0).getTextContent())
                            .build();
                    extendedDescriptions.add(extendedDescription);
                }else{
                    NodeList childNodes = ((Element) extendedDescriptionNode).getChildNodes();
                    for  (int i = 0; i < childNodes.getLength(); i++){
                        Element currentNodeElement =  (Element) childNodes.item(i);
                        CWEExtendedDescription extendedDescription = CWEExtendedDescription.builder()
                                .description(currentNodeElement.getTextContent())
                                .build();
                        extendedDescriptions.add(extendedDescription);
                    }
                }
            }
            return extendedDescriptions;
        } catch (NullPointerException e) {
            return extendedDescriptions;
        }
    }

    private List<CWERelatedWeakness> getElementRelatedWeaknesses(Element eElement){
        List<CWERelatedWeakness> relatedWeaknesses = new ArrayList<>();
        try {
            NodeList relatedWeaknessesNote = eElement.getElementsByTagName("Related_Weakness");

            if (relatedWeaknessesNote.getLength() > 0) {
                for  (int i = 0; i < relatedWeaknessesNote.getLength(); i++){
                    Element currentNodeElement =  (Element) relatedWeaknessesNote.item(i);
                    CWERelatedWeakness relatedWeakness = CWERelatedWeakness.builder()
                            .nature(currentNodeElement.getAttribute("Nature"))
                            .cwe_id(Integer.parseInt(currentNodeElement.getAttribute("CWE_ID")))
                            .build();
                    relatedWeaknesses.add(relatedWeakness);
                }
            }
            return relatedWeaknesses;
        } catch (NullPointerException e) {
            return relatedWeaknesses;
        }
    }

    private List<CWEWeaknessOrdinalities> getElementWeaknessOrdinalities(Element eElement){
        List<CWEWeaknessOrdinalities> weaknessOrdinalities = new ArrayList<>();
        try {
            NodeList weaknessOrdinalitiesNode = eElement.getElementsByTagName("Weakness_Ordinality");

            if (weaknessOrdinalitiesNode.getLength() > 0) {
                for  (int i = 0; i < weaknessOrdinalitiesNode.getLength(); i++){
                    Element currentNodeElement =  (Element) weaknessOrdinalitiesNode.item(i);
                    CWEWeaknessOrdinalities relatedWeakness = CWEWeaknessOrdinalities.builder()
                            .ordinality(currentNodeElement.getElementsByTagName("Ordinality").item(0).getTextContent())
                            .description(currentNodeElement.getElementsByTagName("Description").item(0).getTextContent())
                            .build();
                    weaknessOrdinalities.add(relatedWeakness);
                }
            }
            return weaknessOrdinalities;
        } catch (NullPointerException e) {
            return weaknessOrdinalities;
        }
    }

    private List<CWEApplicablePlatforms> getElementApplicablePlatforms(Element eElement){
        List<CWEApplicablePlatforms> applicablePlatforms = new ArrayList<>();
        try {
            // TODO: currently only the language is extracted, rarely is technology and OS available
            NodeList applicablePlatformsNode = eElement.getElementsByTagName("Language");
            if (applicablePlatformsNode.getLength() > 0) {
                for  (int i = 0; i < applicablePlatformsNode.getLength(); i++){
                    Element currentNodeElement =  (Element) applicablePlatformsNode.item(i);
                    CWEApplicablePlatforms relatedWeakness = CWEApplicablePlatforms.builder()
                            .platformClass(currentNodeElement.getAttribute("Class"))
                            .prevalence(currentNodeElement.getAttribute("Prevalence"))
                            .name(currentNodeElement.getAttribute("Name"))
                            .build();
                    applicablePlatforms.add(relatedWeakness);
                }
            }
            return applicablePlatforms;
        } catch (NullPointerException e) {
            return applicablePlatforms;
        }
    }

    private List<CWEModeOfIntroduction> getElementModesOfIntroduction(Element eElement){
        List<CWEModeOfIntroduction> modesOfIntroduction = new ArrayList<>();
        try {
            NodeList modesOfIntroductionNode = eElement.getElementsByTagName("Introduction");
            if (modesOfIntroductionNode.getLength() > 0) {
                for  (int i = 0; i < modesOfIntroductionNode.getLength(); i++){
                    Element currentNodeElement =  (Element) modesOfIntroductionNode.item(i);
                    CWEModeOfIntroduction modeOfIntroduction = CWEModeOfIntroduction.builder()
                            .phase((currentNodeElement.getElementsByTagName("Phase").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Phase").item(0).getTextContent() : "")
                            .note((currentNodeElement.getElementsByTagName("Note").getLength()  > 0)
                                    ? currentNodeElement.getElementsByTagName("Note").item(0).getTextContent() : "")
                            .build();
                    modesOfIntroduction.add(modeOfIntroduction);
                }
            }
            return modesOfIntroduction;
        } catch (NullPointerException e) {
            return modesOfIntroduction;
        }
    }

    private List<CWECommonConsequence> getElementCommonConsequences(Element eElement){
        List<CWECommonConsequence> commonConsequences = new ArrayList<>();
        try {
            NodeList commonConsequencesNode = eElement.getElementsByTagName("Common_Consequences");
            if (commonConsequencesNode.getLength() > 0) {
                for  (int i = 0; i < commonConsequencesNode.getLength(); i++){
                    Element currentNodeElement =  (Element) commonConsequencesNode.item(i);
                    NodeList commonConsequencesChildNodes = currentNodeElement.getChildNodes();
                    List <String> scope = new ArrayList<String>();
                    List <String> impact = new ArrayList<String>();
                    List <String> note = new ArrayList<String>();

                    for  (int j = 0; j < commonConsequencesChildNodes.getLength(); j++){
                        NodeList childChildNode = commonConsequencesChildNodes.item(j).getChildNodes();

                        for  (int k = 0; k < childChildNode.getLength(); k++){
                            String nodeName = childChildNode.item(k).getNodeName();

                            switch (nodeName){
                                case "Scope":
                                    scope.add(childChildNode.item(k).getTextContent());
                                    break;
                                case "Impact":
                                    impact.add(childChildNode.item(k).getTextContent());
                                    break;
                                case "Note":
                                    note.add(childChildNode.item(k).getTextContent());
                                    break;
                            }
                        }
                    }

                    CWECommonConsequence commonConsequence = CWECommonConsequence.builder()
                            .scope(scope)
                            .impact(impact)
                            .note(note)
                            .build();
                    commonConsequences.add(commonConsequence);
                }
            }
            return commonConsequences;
        } catch (NullPointerException e) {
            return commonConsequences;
        }
    }

    private List<CWEDetectionMethods> getElementDetectionMethods(Element eElement){
        List<CWEDetectionMethods> detectionMethods = new ArrayList<>();
        try {
            NodeList detectionMethodsNode = eElement.getElementsByTagName("Detection_Method");
            if (detectionMethodsNode.getLength() > 0) {
                for  (int i = 0; i < detectionMethodsNode.getLength(); i++){
                    Element currentNodeElement =  (Element) detectionMethodsNode.item(i);

                    CWEDetectionMethods detectionMethod = CWEDetectionMethods.builder()
                            .method((currentNodeElement.getElementsByTagName("Method").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Method").item(0).getTextContent() : "")
                            .description((currentNodeElement.getElementsByTagName("Description").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Description").item(0).getTextContent() : "")
                            .effectiveness((currentNodeElement.getElementsByTagName("Effectiveness").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Effectiveness").item(0).getTextContent() : "")
                            .effectivenessNotes((currentNodeElement.getElementsByTagName("Effectiveness_Notes").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Effectiveness_Notes").item(0).getTextContent() : "")
                            .build();
                    detectionMethods.add(detectionMethod);
                }
            }
            return detectionMethods;
        } catch (NullPointerException e) {
            return detectionMethods;
        }
    }

    private List<CWEPotentialMitigations> getElementPotentialMitigations(Element eElement){
        List<CWEPotentialMitigations> mitigationMethods = new ArrayList<>();
        try {
            NodeList potentialMitigationNode = eElement.getElementsByTagName("Mitigation");
            if (potentialMitigationNode.getLength() > 0) {
                for  (int i = 0; i < potentialMitigationNode.getLength(); i++){
                    Element currentNodeElement =  (Element) potentialMitigationNode.item(i);

                    CWEPotentialMitigations mitigationMethod = CWEPotentialMitigations.builder()
                            .phase((currentNodeElement.getElementsByTagName("Phase").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Phase").item(0).getTextContent() : "")
                            .strategy((currentNodeElement.getElementsByTagName("Strategy").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Strategy").item(0).getTextContent() : "")
                            .description((currentNodeElement.getElementsByTagName("Description").getLength() > 0)
                                    ? currentNodeElement.getElementsByTagName("Description").item(0).getTextContent() : "")
                            .build();
                    mitigationMethods.add(mitigationMethod);
                }
            }
            return mitigationMethods;
        } catch (NullPointerException e) {
            return mitigationMethods;
        }
    }


    private List<String> getElementAffectedResources(Element eElement){
        List<String> affectedResources = new ArrayList<>();
        try {
            NodeList affectedResouceNode = eElement.getElementsByTagName("Affected_Resource");
            if (affectedResouceNode.getLength() > 0) {
                for  (int i = 0; i < affectedResouceNode.getLength(); i++){
                    Element currentNodeElement =  (Element) affectedResouceNode.item(i);
                    affectedResources.add(currentNodeElement.getTextContent());
                }
            }
            return affectedResources;
        } catch (NullPointerException e) {
            return affectedResources;
        }
    }

    private List<Integer> getElementRelatedAttackPatterns(Element eElement){
        List<Integer> relatedAttackPatterns = new ArrayList<>();
        try {
            NodeList affectedResouceNode = eElement.getElementsByTagName("Related_Attack_Pattern");
            if (affectedResouceNode.getLength() > 0) {
                for  (int i = 0; i < affectedResouceNode.getLength(); i++){
                    Element currentNodeElement =  (Element) affectedResouceNode.item(i);
                    relatedAttackPatterns.add(Integer.parseInt(currentNodeElement.getAttribute("CAPEC_ID")));
                }
            }
            return relatedAttackPatterns;
        } catch (NullPointerException e) {
            return relatedAttackPatterns;
        }
    }



    private List<String> getCWEFileList() {
        List<String> cweFiles = new ArrayList<>();
        Collections.addAll(cweFiles, "src/main/resources/1000.xml", "src/main/resources/699.xml");
        return cweFiles;
    }

}
