package com.tam.model.MitreCWE;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CWECommonConsequence {
    private List<String> scope;
    private List<String> impact;
    private List<String> note;

}
