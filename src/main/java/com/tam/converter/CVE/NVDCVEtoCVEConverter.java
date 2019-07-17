package com.tam.converter.CVE;

import com.tam.model.*;
import com.tam.model.NVDCVE.*;
import com.tam.utils.CVEConvertUtil;

import java.util.List;
import java.util.stream.Collectors;

public class NVDCVEtoCVEConverter {

    public static List<CVEResource> convertNVDCVEsToCVEs(List<NVDCVE> nvdcves) {
        return nvdcves.stream().map(NVDCVEtoCVEConverter::convert).collect(Collectors.toList());
    }

    public static CVEResource convert(NVDCVE nvdcve) {
        return new CVEResource()
                .cveID(nvdcve.getCve().getCVE_data_meta().getID())
                .assigner(nvdcve.getCve().getCVE_data_meta().getASSIGNER())
                .dataFormat(nvdcve.getCve().getData_format())
                .dataVersion(nvdcve.getCve().getData_version())
                .affects(convertNVDVendorDataListVendorList(nvdcve.getCve().getAffects().getVendor().getVendor_data()))
                .problemTypes(convertNVDProblemTypeListToProblemTypeList(nvdcve.getCve().getProblemtype().getProblemtype_data()))
                .references(convertNVDReferenceDataListToReferenceList(nvdcve.getCve().getReferences().getReference_data()))
                .descriptions(convertNVDVulnerabilityDescriptionListToVulnerabilityDescriptionList(nvdcve.getCve().getDescription().getDescription_data()))
                .vulnerableConfig(convertNVDVulnerableConfigDataToVulnerableConfigData(nvdcve.getConfigurations()))
                .cvss(convertNVDCVSSToCVSS(nvdcve.getImpact()))
                .publishedDate(nvdcve.getPublishedDate())
                .lastModifiedDate(nvdcve.getLastModifiedDate());
    }

    private static CVSSResource convertNVDCVSSToCVSS(NVDImpact impact) {
        CVSSResource cvss = new CVSSResource();
        if (impact.getBaseMetricV2() != null) {
            cvss.setCvssv2Metric(convertNVDCVSSV2toCVSSV2(impact.getBaseMetricV2()));
        }
        if (impact.getBaseMetricV3() != null) {
            cvss.setCvssv3Metric(convertNVDCVSSV3toCVSSV3(impact.getBaseMetricV3()));
        }
        return cvss;
    }

    private static CVSSV2MetricResource convertNVDCVSSV2toCVSSV2(NVDBaseMetricV2 baseMetricV2) {
        return new CVSSV2MetricResource()
                .version(baseMetricV2.getCvssV2().getVersion())
                .vectorString(baseMetricV2.getCvssV2().getVectorString())
                .exploitabilityScore(baseMetricV2.getExploitabilityScore())
                .impactScore(baseMetricV2.getImpactScore())
                .baseScore(baseMetricV2.getCvssV2().getBaseScore())
                .acInsufInfo(baseMetricV2.isAcInsufInfo())
                .obtainAllPrivilege(baseMetricV2.isObtainAllPrivilege())
                .obtainUserPrivilege(baseMetricV2.isObtainUserPrivilege())
                .obtainOtherPrivilege(baseMetricV2.isObtainOtherPrivilege())
                .userInteractionRequired(baseMetricV2.isUserInteractionRequired())
                .accessVector(baseMetricV2.getCvssV2().getAccessVector())
                .accessComplexity(baseMetricV2.getCvssV2().getAccessComplexity())
                .authentication(baseMetricV2.getCvssV2().getAuthentication())
                .confidentialityImpact(baseMetricV2.getCvssV2().getConfidentialityImpact())
                .integrityImpact(baseMetricV2.getCvssV2().getIntegrityImpact())
                .availabilityImpact(baseMetricV2.getCvssV2().getAvailabilityImpact());
    }

    private static CVSSV3MetricResource convertNVDCVSSV3toCVSSV3(NVDBaseMetricV3 baseMetricV3) {
        return new CVSSV3MetricResource()
                .baseScore(baseMetricV3.getCvssV3().getBaseScore())
                .exploitabilityScore(baseMetricV3.getExploitabilityScore())
                .impactScore(baseMetricV3.getImpactScore())
                .baseSeverity(baseMetricV3.getCvssV3().getBaseSeverity())
                .version(baseMetricV3.getCvssV3().getVersion())
                .vectorString(baseMetricV3.getCvssV3().getVectorString())
                .attackVector(CVEConvertUtil.buildAttackVectorEnumFromString(baseMetricV3.getCvssV3().getAttackVector()))
                .attackComplexity(CVEConvertUtil.buildAttackComplexityEnumFromString(baseMetricV3.getCvssV3().getAttackComplexity()))
                .privilegesRequired(CVEConvertUtil.buildRequriedPrivilegesEnumFromString(baseMetricV3.getCvssV3().getPrivilegesRequired()))
                .userInteraction(CVEConvertUtil.buildUserInteractionEnumFromString(baseMetricV3.getCvssV3().getUserInteraction()))
                .scope(CVEConvertUtil.buildScopeEnumFromString(baseMetricV3.getCvssV3().getScope()))
                .confidentialityImpact(CVEConvertUtil.buildImpactEnumFromString(baseMetricV3.getCvssV3().getConfidentialityImpact()))
                .integrityImpact(CVEConvertUtil.buildImpactEnumFromString(baseMetricV3.getCvssV3().getIntegrityImpact()))
                .availabilityImpact(CVEConvertUtil.buildImpactEnumFromString(baseMetricV3.getCvssV3().getAvailabilityImpact()));
    }

    private static VulnerableConfigurationResource convertNVDVulnerableConfigDataToVulnerableConfigData(NVDConfiguration configurations) {
        return new VulnerableConfigurationResource()
                .cveDataVersion(configurations.getCVE_data_version())
                .configs(convertNVDConfigListToConfigList(configurations.getNodes()));
    }

    private static List<ConfigResource> convertNVDConfigListToConfigList(List<NVDNode> nodes) {
        return nodes.stream().map(NVDCVEtoCVEConverter::convertNVDConfigToConfig).collect(Collectors.toList());
    }

    private static ConfigResource convertNVDConfigToConfig(NVDNode node) {
        ConfigResource config = new ConfigResource();
        config.setOperator(node.getOperator().equals("AND") ? OperatorResource.AND : OperatorResource.OR);
        if (config.getOperator().equals(OperatorResource.AND) && node.getChildren() != null) {
            config.setChildren(convertNVDConfigListToConfigList(node.getChildren()));
        } else if (node.getCpe_match() != null){
            config.setCpeMatches(convertNVDCPEMatchListToCpeMatchList(node.getCpe_match()));
        }
        return config;
    }

    private static List<CPEMatchResource> convertNVDCPEMatchListToCpeMatchList(List<NVDCPEMatch> cpeMatches) {
        return cpeMatches.stream().map(cpeMatch
                -> new CPEMatchResource()
                .vulnerable(cpeMatch.isVulnerable())
                .cpe23URI(cpeMatch.getCpe23Uri())
                .versionStartIncluding(cpeMatch.getVersionStartIncluding())
                .versionStartExcluding(cpeMatch.getVersionStartExcluding())
                .versionEndIncluding(cpeMatch.getVersionEndIncluding())
                .versionEndExcluding(cpeMatch.getVersionEndExcluding())).collect(Collectors.toList());
    }

    private static List<VulnerabilityDescriptionResource> convertNVDVulnerabilityDescriptionListToVulnerabilityDescriptionList(List<NVDDescriptionData> descriptionDataList) {
        return descriptionDataList
                .stream()
                .map(descriptionData
                        -> new VulnerabilityDescriptionResource()
                        .language(descriptionData.getLang())
                        .content(descriptionData.getValue()))
                .collect(Collectors.toList());
    }

    private static List<ReferenceResource> convertNVDReferenceDataListToReferenceList(List<NVDReferenceData> references) {
        return references.stream().map(reference ->
                new ReferenceResource()
                        .url(reference.getUrl())
                        .name(reference.getName())
                        .refSource(reference.getRefsource())
                        .tags(reference.getTags()))
                .collect(Collectors.toList());
    }

    private static List<ProblemTypeResource> convertNVDProblemTypeListToProblemTypeList(List<NVDProblemTypeData> problemtype_data) {
        return problemtype_data
                .stream()
                .map(NVDCVEtoCVEConverter::convertNVDProblemTypeToProblemType)
                .collect(Collectors.toList());
    }

    private static ProblemTypeResource convertNVDProblemTypeToProblemType(NVDProblemTypeData problemtypeData) {
        return new ProblemTypeResource()
                .descriptions(convertNVDProblemTypeDescriptionListToProblemTypeDescriptionList(problemtypeData.getDescription()));
    }

    private static List<ProblemTypeDescriptionResource> convertNVDProblemTypeDescriptionListToProblemTypeDescriptionList(List<NVDDescriptionData> descriptions) {
        return descriptions.stream()
                .map(description -> new ProblemTypeDescriptionResource()
                        .cweID(description.getValue())
                        .language(description.getLang()))
                .collect(Collectors.toList());
    }

    private static List<VendorDataResource> convertNVDVendorDataListVendorList(List<NVDVendorData> vendor) {
        return vendor
                .stream()
                .map(NVDCVEtoCVEConverter::convertNVDCVEVendorDataSetToCVEVendorDataSet).collect(Collectors.toList());
    }

    private static VendorDataResource convertNVDCVEVendorDataSetToCVEVendorDataSet(NVDVendorData vendordata) {
        return new VendorDataResource()
                .vendorName(vendordata.getVendor_name())
                .products(convertNVDProductDataListToProductDataList(vendordata.getProduct().getProduct_data()));
    }

    private static List<ProductResource> convertNVDProductDataListToProductDataList(List<NVDProductData> product_data) {
        return product_data.stream().map(NVDCVEtoCVEConverter::convertNVDProductToProduct).collect(Collectors.toList());
    }

    private static ProductResource convertNVDProductToProduct(NVDProductData productData) {
        return new ProductResource()
                .productName(productData.getProduct_name())
                .versionData(convertNVDVersionDataListToVersionDataList(productData.getVersion().getVersion_data()));
    }

    private static List<VersionDataResource> convertNVDVersionDataListToVersionDataList(List<NVDVersionData> version_data) {
        return version_data.stream().map(versionData
                -> new VersionDataResource()
            .versionValue(versionData.getVersion_value())
            .versionAffected(versionData.getVersion_affected()))
                .collect(Collectors.toList());
    }
}
