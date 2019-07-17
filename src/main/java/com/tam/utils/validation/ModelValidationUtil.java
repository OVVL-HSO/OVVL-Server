package com.tam.utils.validation;

import com.tam.model.BaseDFDModelResource;
import com.tam.model.BaseModelStorageDataResource;

public class ModelValidationUtil {

    public static boolean baseDFDModelIsInvalid(BaseDFDModelResource dfdModel) {
        // TODO: More specific validation
        return dfdModelContainsNoElements(dfdModel)
                || dfdModel.getStorageData() == null
                || dfdModel.getStorageData().getScreenshot() == null
                || storageDataIsNotValid(dfdModel.getStorageData());
    }

    private static boolean storageDataIsNotValid(BaseModelStorageDataResource storageData) {
        return storageData.getName().length() > 25
                || storageData.getName().length() < 3
                || storageData.getSummary().length() > 300
                || storageData.getSummary().length() < 5
                || fileSizeOfScreenshotIsLargerThan500KB(storageData);
    }

    private static boolean fileSizeOfScreenshotIsLargerThan500KB(BaseModelStorageDataResource storageData) {
        // https://en.wikipedia.org/wiki/Base64#Padding
        return ((storageData.getScreenshot().getBytes().length * 3/4 - 2) / 1000  > 500);
    }

    private static boolean dfdModelContainsNoElements(BaseDFDModelResource dfdModel) {
        return dfdModel.getInteractors() == null || dfdModel.getProcesses() == null || dfdModel.getDataStores() == null;
    }
}
