package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenericModelElement {
    private String elementID;
    private ElementTypeResource elementType;
}
