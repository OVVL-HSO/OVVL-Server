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
    private List<AppliedStrideThreatResource> foundThreats;
    private List<AppliedCweThreatResource> foundCWEThreats;

    public WorkingArea(String username,
                AnalysisDFDModelResource currentModel,
                List<AppliedStrideThreatResource> foundThreats,
                List<AppliedCweThreatResource> foundCWEThreats) {
        this.username = username;
        this.currentModel = currentModel;
        this.foundThreats = foundThreats;
        this.foundCWEThreats = foundCWEThreats;
    }
}
