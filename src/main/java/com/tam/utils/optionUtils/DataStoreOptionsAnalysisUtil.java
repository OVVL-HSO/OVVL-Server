package com.tam.utils.optionUtils;

import com.tam.model.DataStoreOptionsResource;
import com.tam.model.GenericSelectionResource;
import com.tam.model.ScaleSelectionResource;

public class DataStoreOptionsAnalysisUtil {
    public static boolean dataStoreStoresSensitiveDataAndIsntHighlyTrusted(DataStoreOptionsResource options) {
        return options.getTrustLevel() != ScaleSelectionResource.HIGH && dataStoreIsNotHighlyTrusted(options);
    }

    public static boolean dataStoreIsNotHighlyTrusted(DataStoreOptionsResource options) {
        return options.getStoresSensitiveData() == GenericSelectionResource.YES;
    }

    public static boolean dataStoreStoresLogData(DataStoreOptionsResource options) {
        return options.getStoresLogs() == GenericSelectionResource.YES;
    }

    public static boolean dataStoreIsEncrypted(DataStoreOptionsResource options) {
        return options.getEncrypted() == GenericSelectionResource.YES;
    }
}
