package com.tam.model.MitreCWE.ThreatCatalogue;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CWEThreatItem {
    private String accessVector;
    private String authentication;
    private String language;
    private String technology;
    private int affectedRows;
    private List<CWEPotentialWeakness> potentialWeaknesses;
}
