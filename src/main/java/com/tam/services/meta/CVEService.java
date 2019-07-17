package com.tam.services.meta;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tam.converter.CVE.NVDCVEtoCVEConverter;
import com.tam.model.*;
import com.tam.model.NVDCVE.NVDCVEJson;
import com.tam.model.NVDCVE.NVDCVE;
import com.tam.repositories.CVERepository;
import com.tam.utils.CheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CVEService {

    @Autowired
    private CVERepository cveRepository;

    public List<CVEResource> findCVEDataAffectingCPE(String cpe, List<CVEResource> cves) {
        VendorData vendorData = extractVendorDataFromCPE(cpe);
        return cves.stream()
                .filter(cve -> cveMetaDataAppliesToVendorData(cve.getAffects(), vendorData))
                .collect(Collectors.toList());
    }

    private boolean cveMetaDataAppliesToVendorData(List<VendorDataResource> vendorDataSet, VendorData vendorData) {
        return vendorDataSet.stream().anyMatch(productData
                -> CheckUtil.twoProductVendorsMatchOrVendorIsWildcard(productData.getVendorName(), vendorData.getVendor())
                && CheckUtil.productNameAndVersionMatchOrAreWildcard(productData.getProducts(), vendorData));
    }

    private VendorData extractVendorDataFromCPE(String cpe) {
        String vendor = "", product = "", version = "";
        // cpe:2.3:a:10web:form_maker:1.13.5:*:*:*:*:wordpress:*:*
        // 1. Remove unnecessary part of CPEItem string
        if(cpe.length() > 10) {
            cpe = cpe.substring(10);
            // -> 10web:form_maker:1.13.5:*:*:*:*:wordpress:*:*
        }
        // 2. Get the vendor
        if (cpe.contains(":")) {
            vendor = cpe.substring(0, cpe.indexOf(":"));
            cpe = cpe.substring(cpe.indexOf(":") + 1);
            // -> form_maker:1.13.5:*:*:*:*:wordpress:*:*
        }
        // 3. Get the product
        if (cpe.contains(":")) {
            product = cpe.substring(0, cpe.indexOf(":"));
            cpe = cpe.substring(cpe.indexOf(":") + 1);
            // -> 1.13.5:*:*:*:*:wordpress:*:*
        }
        // 4. Get the version
        if (cpe.contains(":")) {
            version = cpe.substring(0, cpe.indexOf(":"));
        }
        return new VendorData(vendor, product, version);
    }

    public void fillDBWithCVEData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        List<String> fileSpecifications = getNVDFileSpecifications();
        List<CVEResource> cves = new ArrayList<>();
        for (String specification : fileSpecifications) {
            cves = Objects.requireNonNull(convertNVDCVEFileInputDataToNVDCVELists(mapper, specification, cves));
        }
        cveRepository.saveAll(cves);
    }

    private List<String> getNVDFileSpecifications() {
        List<String> fileSpecifications = new ArrayList<>();
        Collections.addAll(fileSpecifications, "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "modified");
        return fileSpecifications;
    }

    private List<CVEResource> convertNVDCVEFileInputDataToNVDCVELists(ObjectMapper mapper,
                                                                      String fileSpecification,
                                                                      List<CVEResource> alreadyMappedCVEs) {
        TypeReference<NVDCVEJson> typeReference = new TypeReference<NVDCVEJson>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/nvdcve-1.0-" +  fileSpecification + ".json");
        try {
            // 1. Parse the JSON containing the CVE data and map it to the NVDCVEJson data structure
            NVDCVEJson nvdcveJson = mapper.readValue(inputStream, typeReference);
            // 2. Collect all the CVEs
            List<NVDCVE> nvdCVEs = nvdcveJson.getCVE_Items();
            // 3. Map these CVEs to the structure OVVL is using
            List<CVEResource> cves = NVDCVEtoCVEConverter.convertNVDCVEsToCVEs(nvdCVEs);
            // 4. Combine them with the existing CVEs, and remove already existing CVEs if new ones "overwrite" them
            return combineCVEResourceListsWithoutDuplicates(cves, alreadyMappedCVEs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CVEResource> combineCVEResourceListsWithoutDuplicates(List<CVEResource> newCVEs,
                                                                       List<CVEResource> alreadyMappedCVEs) {
        // We remove every CVE that is included in the new list
        alreadyMappedCVEs.removeIf(cve -> newCVEs.stream().anyMatch(newCve -> cve.getCveID().equals(newCve.getCveID())));
        // Then we combine the two lists
        newCVEs.addAll(alreadyMappedCVEs);
        return newCVEs;
    }

    @Cacheable("cveCache")
    public List<CVEResource> loadAllCVES() {
        return cveRepository.findAll();
    }
}
