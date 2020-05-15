package com.tam.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// Used to store the analysis data of an analyzed DFDModel
public class WorkingArea {
    private String username;
    private AnalysisDFDModelResource currentModel;
    private List<AppliedSTRIDEThreatResource> foundThreats;

    public WorkingArea(String username,
                AnalysisDFDModelResource currentModel,
                List<AppliedSTRIDEThreatResource> foundThreats) {
        this.username = username;
        this.currentModel = currentModel;
        this.foundThreats = foundThreats;
    }
}
