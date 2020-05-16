package com.tam.model.MitreCWE.ThreatCatalogue.AttributeSelection;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CWEThreatTableSelectionAttributes {
    private CWESelectionAccessVector accessVector;
    private CWESelectionAuthentication authentication;
    private CWESelectionLanguage language;
    private CWESelectionTechnology technology;
}
