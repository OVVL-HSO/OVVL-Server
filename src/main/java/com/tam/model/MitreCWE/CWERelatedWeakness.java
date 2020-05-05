package com.tam.model.MitreCWE;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CWERelatedWeakness {
    private String nature;
    private Integer cwe_id;
}

