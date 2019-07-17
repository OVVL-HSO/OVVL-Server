package com.tam.converter;

import com.tam.model.ModelStorageDataResource;
import com.tam.model.ModelStorageData;

import java.util.List;
import java.util.stream.Collectors;

public class ModelStorageDataEntityToResourceConverter {

    public static List<ModelStorageDataResource> convertStorageDataEntitiesToResources(List<ModelStorageData> storageDataEntities) {
        return storageDataEntities.stream().map(ModelStorageDataEntityToResourceConverter::convert).collect(Collectors.toList());
    }

    public static ModelStorageDataResource convert(ModelStorageData storageData) {
        return new ModelStorageDataResource()
                .modelID(storageData.getModelID())
                .name(storageData.getName())
                .date(storageData.getDate())
                .projectTitle(storageData.getProjectTitle())
                .summary(storageData.getSummary())
                .owner(storageData.getUsername())
                .projectID(storageData.getProjectID())
                .screenshot(storageData.getScreenshot());
    }
}
