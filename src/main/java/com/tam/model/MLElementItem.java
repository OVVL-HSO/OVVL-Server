package com.tam.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MLElementItem {
    ElementTypeResource genericType;
    Object type;
    Object options;

    public MLElementItem(ElementTypeResource genericType, Object type, Object options) {
        this.genericType = genericType;
        this.type = type;
        this.options = options;
    }
}
