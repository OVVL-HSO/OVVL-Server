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
    private List<AppliedThreatResource> foundThreats;

    public WorkingArea(String username,
                AnalysisDFDModelResource currentModel,
                List<AppliedThreatResource> foundThreats) {
        this.username = username;
        this.currentModel = currentModel;
        this.foundThreats = foundThreats;
    }
}
