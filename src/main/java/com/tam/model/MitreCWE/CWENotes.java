package com.tam.model.MitreCWE;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CWENotes {
    private String type;
    private String text;
}
