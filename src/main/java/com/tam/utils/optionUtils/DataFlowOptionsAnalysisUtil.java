package com.tam.utils.optionUtils;


import com.tam.model.DataFlowOptionsResource;
import com.tam.model.DataFlowTypeResource;
import com.tam.model.GenericSelectionResource;
import com.tam.model.NetworkTypeResource;

public class DataFlowOptionsAnalysisUtil {
    public static boolean dataFlowUsesNetwork(DataFlowOptionsResource options) {
        return options.getNetworkType() != NetworkTypeResource.OTHER;
    }

    public static boolean dataFlowTransportsSensitiveData(DataFlowOptionsResource options) {
        return options.getTransfersSensitiveData() == GenericSelectionResource.TRUE;
    }

    public static boolean dataFlowNotSecure(DataFlowTypeResource type) {
        return type == DataFlowTypeResource.HTTP;
    }

    public static boolean dataFlowIsNotTrusted(DataFlowOptionsResource options) {
        return options.getTrustedNetwork() == GenericSelectionResource.FALSE;
    }
}
