package com.tam.utils.optionUtils;

import com.tam.model.GenericSelectionResource;
import com.tam.model.ProcessOptionsResource;
import com.tam.model.ProcessTypeResource;
import com.tam.model.ScaleSelectionResource;

public class ProcessOptionsAnalysisUtil {
    public static boolean processTrustLevelIsNotHigh(ProcessOptionsResource options) {
        return options.getTrustLevel() != ScaleSelectionResource.HIGH;
    }

    public static boolean processUsesNetwork(ProcessTypeResource type) {
        return type != ProcessTypeResource.THREAD;
    }

    public static boolean processRequiresAuthentication(ProcessOptionsResource options) {
        return options.getRequiresAuthentication() == GenericSelectionResource.TRUE;
    }

    public static boolean processUsesCustomProtocol(ProcessOptionsResource options) {
        return options.getCustomCommunicationProtocol() == GenericSelectionResource.TRUE;
    }

    public static boolean bothCommunicatingProcessesArentHighlyTrusted(ProcessOptionsResource options, ProcessOptionsResource secondOptions) {
        return (options.getTrustLevel() != ScaleSelectionResource.HIGH || options.getTrustLevel() != ScaleSelectionResource.MEDIUM)
                && (secondOptions.getTrustLevel() != ScaleSelectionResource.HIGH || secondOptions.getTrustLevel() != ScaleSelectionResource.MEDIUM);
    }

    public static boolean processImplementsDDoSProtection(ProcessOptionsResource options) {
        return options.getDdosProtection() == GenericSelectionResource.TRUE;
    }

    public static boolean processMightAllowUserGeneratedContent(ProcessTypeResource type) {
        return type == ProcessTypeResource.APPLICATION;
    }

    public static boolean processDoesNotSanitizeInput(ProcessOptionsResource options) {
        return options.getSanitizesInput() != GenericSelectionResource.FALSE;
    }
}
