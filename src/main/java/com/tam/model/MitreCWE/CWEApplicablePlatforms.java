package com.tam.model.MitreCWE;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CWEApplicablePlatforms {
    private String name;
    private String platformClass;
    private String prevalence;
}
