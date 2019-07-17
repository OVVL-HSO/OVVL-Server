package com.tam.utils;

import com.tam.model.ConnectedElementResource;
import com.tam.model.GenericModelElement;

// Extracted function to convert elements saved in a dataFlow to generic-elements, so no unnecessary data is passed
public class GenericElementUtil {
    public static GenericModelElement convertDataFlowConnectedElementsToGenericElement(ConnectedElementResource connectedElement) {
        return GenericModelElement.builder()
                .elementID(connectedElement.getId())
                .elementType(connectedElement.getType())
                .build();
    }
}
