package com.tam.services.meta;

import com.tam.converter.CPEEntityToResourceConverter;
import com.tam.model.*;
import com.tam.repositories.CPERepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
public class CPEService {

    private CPERepository cpeRepository;

    @Autowired
    public CPEService(CPERepository cpeRepository) {
        this.cpeRepository = cpeRepository;
    }

    public List<CPEResource> findIemsMatchingProductQuery(String searchQuery) {
        List<CPEResource> foundCPEItems = new ArrayList<>();
        List<CPEItem> cpes = cpeRepository.findAllByTitleMatchesRegex(searchQuery);
        if (cpes != null) {
            foundCPEItems = CPEEntityToResourceConverter.convertCPEEntitiesToResources(cpes);
        }
        return foundCPEItems;
    }

    public void fillDBWithCPEXML() {
        // XML from here: https://nvd.nist.gov/Products/CPE#
        try {
            File fXmlFile = new File("src/main/resources/official-cpe-dictionary_v2.3.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();


            NodeList nList = doc.getElementsByTagName("cpe-item");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    CPEItem cpe = CPEItem.builder()
                            .id(temp)
                            .cpeName(eElement.getAttribute("name"))
                            .title(eElement.getElementsByTagName("title").item(0).getTextContent())
                            .references(getElementReference(eElement))
                            .cpe23Name(eElement.getElementsByTagName("cpe-23:cpe23-item").item(0).getAttributes().getNamedItem("name").getNodeValue())
                            .build();
                    System.out.println("Current CPE Nr.: " + temp + " of " + nList.getLength());
                    cpeRepository.save(cpe);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private static List<CPEReference> getElementReference(Element eElement) {
        List<CPEReference> referenceList = new ArrayList<>();
        try {
            String referenceTypeContent1 = eElement.getElementsByTagName("reference").item(0).getAttributes().getNamedItem("href").getNodeValue();
            String referenceTypeElement1 = eElement.getElementsByTagName("reference").item(0).getTextContent();
            CPEReference firstReference = CPEReference.builder()
                    .referenceType(referenceTypeElement1)
                    .referenceContent(referenceTypeContent1)
                    .build();
            referenceList.add(firstReference);

            String referenceTypeContent2 = eElement.getElementsByTagName("reference").item(1).getAttributes().getNamedItem("href").getNodeValue();
            String referenceTypeElement2 = eElement.getElementsByTagName("reference").item(1).getTextContent();
            CPEReference secondReference = CPEReference.builder()
                    .referenceType(referenceTypeElement2)
                    .referenceContent(referenceTypeContent2)
                    .build();
            referenceList.add(secondReference);
            return referenceList;
        } catch (NullPointerException e) {
            return referenceList;
        }
    }

}
