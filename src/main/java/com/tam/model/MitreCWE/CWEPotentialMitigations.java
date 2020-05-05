package com.tam.model.MitreCWE;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CWEPotentialMitigations {
    private String phase;
    private String strategy;
    private String description;
}
