package com.tam.model.MitreCWE.ThreatCatalogue;



import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CWEPotentialWeakness {
    private int cweID;
    private float cwePercentageInSelection;
    private float spoofingProportion;
    private float tamperingProportion;
    private float repudiationProportion;
    private float informationDisclosureProportion;
    private float denialOfServiceProportion;
    private float elevationOfPrivilegeProportion;
    private float meanBaseScore;
    private float meanImpactScore;
    private float meanExploitabilityScore;
}
