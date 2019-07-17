package com.tam.utils;

import com.tam.model.*;

import java.util.List;

public class CheckUtil {

    public static boolean stringExistsInAListOfStrings(List<String> existingStrings,
                                                       String newString) {
        return existingStrings.contains(newString);
    }

    static boolean twoInteractorsAreDifferent(AnalysisInteractorResource updatedInteractor,
                                              AnalysisInteractorResource existingInteractor) {
        return !(updatedInteractor.equals(existingInteractor)
                && updatedInteractor.getOptions().equals(existingInteractor.getOptions()));
    }

    static boolean twoPrcessesAreDifferent(AnalysisProcessResource updatedProcess,
                                           AnalysisProcessResource existingProcess) {
        return !(updatedProcess.equals(existingProcess)
                && updatedProcess.getOptions().equals(existingProcess.getOptions()));
    }

    static boolean twoDataStoresAreDifferent(AnalysisDataStoreResource updatedDataStore,
                                             AnalysisDataStoreResource existingDataStore) {
        return !(updatedDataStore.equals(existingDataStore)
                && updatedDataStore.getOptions().equals(existingDataStore.getOptions()));
    }

    static boolean twoDataFlowsAreDifferent(AnalysisDataFlowResource updatedDataFlow,
                                            AnalysisDataFlowResource existingDataFlow) {
        return !(updatedDataFlow.equals(existingDataFlow)
                && updatedDataFlow.getOptions().equals(existingDataFlow.getOptions()));
    }

    static boolean interactorsWereRemoved(List<AnalysisInteractorResource> newInteractors,
                                          List<AnalysisInteractorResource> oldInteractors) {
        return newInteractors.size() < oldInteractors.size();
    }

    static boolean processesWereRemoved(List<AnalysisProcessResource> newProcesses,
                                        List<AnalysisProcessResource> oldProcesses) {
        return newProcesses.size() < oldProcesses.size();
    }

    static boolean dataStoresWereRemoved(List<AnalysisDataStoreResource> newDataStores,
                                         List<AnalysisDataStoreResource> oldDataStores) {
        return newDataStores.size() < oldDataStores.size();
    }

    static boolean dataFlowsWereRemoved(List<AnalysisDataFlowResource> newDataFlows,
                                        List<AnalysisDataFlowResource> oldDataFlows) {
        return newDataFlows.size() < oldDataFlows.size();
    }

    public static boolean twoProductVendorsMatchOrVendorIsWildcard(String vendorName, String vendorToCompare) {
        return (vendorName.equals(vendorToCompare) || vendorToCompare.equals("*"));
    }

    public static boolean productNameAndVersionMatchOrAreWildcard(List<ProductResource> products, VendorData vendorData) {
        return products
                .stream()
                .anyMatch(singleProduct
                        -> (productNamesMatchOrNameIsWildcard(singleProduct.getProductName(), vendorData.getProduct()))
                        && versionDataAppliesSuppliedVersion(singleProduct.getVersionData(), vendorData));
    }

    private static boolean versionDataAppliesSuppliedVersion(List<VersionDataResource> versionDataList, VendorData vendorData) {
        return versionDataList.stream().anyMatch(versionData
                -> versionData.getVersionValue().equals(vendorData.getVersion())
                || vendorData.getVersion().equals("*")
                || vendorData.getVersion().equals(""));
    }

    private static boolean productNamesMatchOrNameIsWildcard(String productName, String productNameToCompare) {
        return productName.equals(productNameToCompare) || productNameToCompare.equals("*") || productNameToCompare.equals("");
    }

    public static boolean dfdModelIncludesCPE(AnalysisDFDModelResource dfdModel) {
        return dfdModel.getInteractors().stream().anyMatch(interactor -> !interactor.getCpe().equals(""))
                || dfdModel.getProcesses().stream().anyMatch(process -> !process.getCpe().equals(""))
                || dfdModel.getDataStores().stream().anyMatch(dataStore -> !dataStore.getCpe().equals(""))
                || dfdModel.getDataFlows().stream().anyMatch(dataFlow -> !dataFlow.getCpe().equals(""));
    }
}
