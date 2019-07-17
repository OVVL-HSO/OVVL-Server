package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CPEReference {
    private String referenceType;
    private String referenceContent;
}
