package com.tam.services;

import com.tam.model.*;
import com.tam.utils.optionUtils.DataFlowOptionsAnalysisUtil;
import com.tam.utils.optionUtils.DataStoreOptionsAnalysisUtil;
import com.tam.utils.optionUtils.InteractorOptionsAnalysisUtil;
import com.tam.utils.optionUtils.ProcessOptionsAnalysisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThreatFinderServiceSTRIDE {

    private ThreatGenerationService threatGenerationService;

    @Autowired
    public ThreatFinderServiceSTRIDE(ThreatGenerationService threatGenerationService) {
        this.threatGenerationService = threatGenerationService;
    }

    void findDataFlowThreats(List<STRIDEThreatResource> foundThreats, AnalysisDataFlowResource dataFlow, ThreatMetaData threatMetaData) {
        if (DataFlowOptionsAnalysisUtil.dataFlowTransportsSensitiveData(dataFlow.getOptions())) {
            foundThreats.add(threatGenerationService.getDataManipulationThreat(threatMetaData));
        }
        if (DataFlowOptionsAnalysisUtil.dataFlowNotSecure(dataFlow.getType()) || DataFlowOptionsAnalysisUtil.dataFlowIsNotTrusted(dataFlow.getOptions())) {
            foundThreats.add(threatGenerationService.getDataFlowInterceptThreat(threatMetaData));
        }
    }

    void findThreatsForProcessStartAndDataStoreEnd(ThreatMetaData threatMetaData,
                                                   AnalysisDataStoreResource endDataStore,
                                                   List<STRIDEThreatResource> foundThreats) {
        if (DataStoreOptionsAnalysisUtil.dataStoreIsEncrypted(endDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getDataStoreEncryptedThreat(endDataStore.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getEncryptionKeyDiscoveryThreat(endDataStore.getName(), threatMetaData));
        }
        if (!DataStoreOptionsAnalysisUtil.dataStoreIsEncrypted(endDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getDataStoreNotEncryptedThreat(endDataStore.getName(), threatMetaData));
        }
        if (DataStoreOptionsAnalysisUtil.dataStoreIsNotHighlyTrusted(endDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getDataStoreWriteThreat(endDataStore.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getDataStoreACLThreat(endDataStore.getName(), threatMetaData));
        }
        if (!DataStoreOptionsAnalysisUtil.dataStoreStoresLogData(endDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getNoLogsThreat(threatMetaData));
        }
        if (DataStoreOptionsAnalysisUtil.dataStoreStoresLogData(endDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getLogReaderAttackThreat(endDataStore.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getLogInfoThreat(endDataStore.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getLogTimeStampThreat(threatMetaData));
            foundThreats.add(threatGenerationService.getArbitraryLogDataThreat(threatMetaData));
        }
    }

    void findThreatsForDataStoreStartAndProcessEnd(ThreatMetaData threatMetaData,
                                                   AnalysisDataStoreResource startDataStore,
                                                   AnalysisProcessResource endProcess,
                                                   AnalysisDataFlowResource dataFlow,
                                                   List<STRIDEThreatResource> foundThreats) {
        addThreatsApplyingToEndProcesses(endProcess, dataFlow.getOptions(), threatMetaData, foundThreats);
        if (DataStoreOptionsAnalysisUtil.dataStoreStoresSensitiveDataAndIsntHighlyTrusted(startDataStore.getOptions())) {
            foundThreats.add(threatGenerationService.getCredentialThiefThreat(startDataStore.getName(), threatMetaData));
        }


    }

    void findThreatsForProcessStartAndProcessEnd(ThreatMetaData threatMetaData,
                                                 AnalysisProcessResource startProcess,
                                                 AnalysisProcessResource endProcess,
                                                 AnalysisDataFlowResource dataFlow,
                                                 List<STRIDEThreatResource> foundThreats) {
        addThreatsApplyingToEndProcesses(endProcess, dataFlow.getOptions(), threatMetaData, foundThreats);
        if(ProcessOptionsAnalysisUtil.bothCommunicatingProcessesArentHighlyTrusted(startProcess.getOptions(), endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getCanonicalNamesThreat(threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processTrustLevelIsNotHigh(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getParameterChangeThreat(endProcess.getName(), threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processMightAllowUserGeneratedContent(startProcess.getType())) {
            foundThreats.add(threatGenerationService.getUserContentThreat(startProcess.getName(), threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processDoesNotSanitizeInput(startProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getInputSanitizationThreat(startProcess.getName(), threatMetaData));
        }
    }

    void findThreatsForInteractorStartAndProcessEnd(ThreatMetaData threatMetaData,
                                                    AnalysisInteractorResource startInteractor,
                                                    AnalysisProcessResource endProcess,
                                                    AnalysisDataFlowResource dataFlow,
                                                    List<STRIDEThreatResource> foundThreats) {
        addThreatsApplyingToEndProcesses(endProcess, dataFlow.getOptions(), threatMetaData, foundThreats);
        foundThreats.add(threatGenerationService.getWeakDigitalSignatureThreat(threatMetaData));
        foundThreats.add(threatGenerationService.getDataCheckThreat(startInteractor.getName(), endProcess.getName(), threatMetaData));
        foundThreats.add(threatGenerationService.getXSSThreat(startInteractor.getName(), threatMetaData));
        if (InteractorOptionsAnalysisUtil.interactorMightAllowUserGeneratedContent(startInteractor.getType())) {
            foundThreats.add(threatGenerationService.getUserContentThreat(startInteractor.getName(), threatMetaData));
        }
        if (InteractorOptionsAnalysisUtil.interactorIsWebAppOrBrowserOrGeneric(startInteractor.getType())) {
            foundThreats.add(threatGenerationService.getClientDoesNotStoreIdentifiersThreat(startInteractor.getName(), threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processRequiresAuthentication(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getPasswordRecoveryThreat(threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processTrustLevelIsNotHigh(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getParameterChangeThreat(endProcess.getName(), threatMetaData));
        }
    }

    private void addThreatsApplyingToEndProcesses(AnalysisProcessResource endProcess,
                                                  DataFlowOptionsResource dataFlowOptions,
                                                  ThreatMetaData threatMetaData,
                                                  List<STRIDEThreatResource> foundThreats) {
        if (ProcessOptionsAnalysisUtil.processTrustLevelIsNotHigh(endProcess.getOptions())
                && ProcessOptionsAnalysisUtil.processUsesNetwork(endProcess.getType())
                && DataFlowOptionsAnalysisUtil.dataFlowUsesNetwork(dataFlowOptions)) {
            foundThreats.add(threatGenerationService.getPortSquatThreat(endProcess.getName(), threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processRequiresAuthentication(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getCredentialsRetryThreat(endProcess.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getPasswordReuseThreat(endProcess.getName(), threatMetaData));
        }
        if (!ProcessOptionsAnalysisUtil.processRequiresAuthentication(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getMissingAuthenticationThreat(endProcess.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getManInTheMiddleThreat(endProcess.getName(), threatMetaData));
        }
        if (ProcessOptionsAnalysisUtil.processUsesCustomProtocol(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getCustomCryptoThreat(endProcess.getName(), threatMetaData));
            foundThreats.add(threatGenerationService.getCustomCommunicationProtocolThreat(endProcess.getName(), threatMetaData));
        }
        if (!ProcessOptionsAnalysisUtil.processImplementsDDoSProtection(endProcess.getOptions())) {
            foundThreats.add(threatGenerationService.getDDoSThreat(endProcess.getName(), threatMetaData));
        }
    }

}
