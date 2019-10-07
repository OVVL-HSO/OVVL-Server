package com.tam.converter;

import com.tam.model.CPEReference;
import com.tam.model.CPEReferenceResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class CPEReferenceEntityToResourceConverter {
    static List<CPEReferenceResource> convertCPEReferenceEntitiesToResources(List<CPEReference> references) {
        return references.stream().map(CPEReferenceEntityToResourceConverter::convert).collect(Collectors.toList());
    }

    static CPEReferenceResource convert(CPEReference reference) {
        return new CPEReferenceResource()
                .referenceType(reference.getReferenceType())
                .referenceContent(reference.getReferenceContent());
    }
/*
    static UserSettings convert(UserSettingsResource test, String username) {
        return new UserSettings().username(username).settings(test);
    }

    static UserSettingsResource convert(UserSettings settings) {
        return new UserSettingsResource().settings(settings.settings)
    }*/
}
