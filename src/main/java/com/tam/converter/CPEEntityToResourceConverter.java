package com.tam.converter;

import com.tam.model.CPEItem;
import com.tam.model.CPEResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CPEEntityToResourceConverter {

    public static List<CPEResource> convertCPEEntitiesToResources(List<CPEItem> cpes) {
        return cpes.stream().map(CPEEntityToResourceConverter::convert).collect(Collectors.toList());
    }

    private static CPEResource convert(CPEItem cpe) {
        return new CPEResource()
                .id(cpe.getId())
                .cpeName(cpe.getCpeName())
                .title(cpe.getTitle())
                .references(CPEReferenceEntityToResourceConverter
                        .convertCPEReferenceEntitiesToResources(cpe.getReferences()))
                .cpe23Name(cpe.getCpe23Name());
    }
}
