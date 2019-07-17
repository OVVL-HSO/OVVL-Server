package com.tam.converter;

import com.tam.model.BaseModelStorageDataResource;
import com.tam.model.ModelStorageData;
import com.tam.utils.GeneralUtil;

public class BaseStorageDataResourceToEntityConverter {
    public static ModelStorageData convert(BaseModelStorageDataResource storageDataResource, String username, String modelID) {
        return ModelStorageData.builder()
                .modelID(modelID)
                .username(username)
                .name(storageDataResource.getName())
                .date(GeneralUtil.getCurrentDateAsISOString())
                .projectTitle(storageDataResource.getProjectTitle())
                .summary(storageDataResource.getSummary())
                .screenshot(storageDataResource.getScreenshot())
                .projectID(storageDataResource.getProjectID())
                .build();
    }
}
