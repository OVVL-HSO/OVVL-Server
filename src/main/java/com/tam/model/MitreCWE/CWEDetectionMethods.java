package com.tam.model.MitreCWE;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CWEDetectionMethods {
    private String method;
    private String description;
    private String effectiveness;
    private String effectivenessNotes;
}
