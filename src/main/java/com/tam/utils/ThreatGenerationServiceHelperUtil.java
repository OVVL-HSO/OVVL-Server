package com.tam.utils;

import com.tam.model.AnalysisDataFlowResource;
import com.tam.model.DataFlowResource;
import com.tam.model.GenericModelElement;
import com.tam.model.ThreatMetaData;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ThreatGenerationServiceHelperUtil {

    public static List<String> createAffectedElementsList(ThreatMetaData threatMetaData) {
        return Stream.of(threatMetaData.getStartElementID(),
                threatMetaData.getEndElementID(),
                threatMetaData.getDataFlowID())
                .collect(Collectors.toList());
    }

    public static ThreatMetaData updateThreatMetaData(ThreatMetaData threatMetaData, String startElementType, String endElementType) {
        return ThreatMetaData.builder()
                .startElementID(threatMetaData.getStartElementID())
                .endElementID(threatMetaData.getEndElementID())
                .startElementType(startElementType)
                .endElementType(endElementType)
                .dataFlowID(threatMetaData.getDataFlowID())
                .build();
    }

    public static ThreatMetaData getRequiredDataForThreatGenerationFromElements(AnalysisDataFlowResource dataFlow) {
        // Convert the elements saved in the dataflow to generic ones, so we don't pass the coordinates all the time
        GenericModelElement dataFlowStartElement = GenericElementUtil.convertDataFlowConnectedElementsToGenericElement(dataFlow.getStartElement());
        GenericModelElement dataFlowEndElement = GenericElementUtil.convertDataFlowConnectedElementsToGenericElement(dataFlow.getEndElement());
        // Convert these elements in one generic element that can be used in threat-generation
        return ThreatMetaData.builder()
                .startElementID(dataFlowStartElement.getElementID())
                .endElementID(dataFlowEndElement.getElementID())
                .startElementType(dataFlowStartElement.getElementType().toString())
                .endElementType(dataFlowEndElement.getElementType().toString())
                .dataFlowID(dataFlow.getId())
                .build();
    }
}
