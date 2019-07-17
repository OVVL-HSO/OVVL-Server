package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
// Meta Data that is passed to the ThreatGenerationService to generate Threats
public class ThreatMetaData {
    private String startElementID;
    private String endElementID;
    private String startElementType;
    private String endElementType;
    private String dataFlowID;
}
