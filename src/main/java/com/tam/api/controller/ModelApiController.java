package com.tam.api.controller;

import com.tam.api.ModelApi;
import com.tam.converter.*;
import com.tam.model.*;
import com.tam.repositories.*;
import com.tam.security.Jwt.JwtProvider;
import com.tam.services.DFDModelAnalysisService;
import com.tam.services.meta.CVEService;
import com.tam.utils.*;
import com.tam.utils.validation.ModelValidationUtil;
import com.tam.utils.validation.ProjectValidationUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@EnableAutoConfiguration
@RestController
public class ModelApiController implements ModelApi {

    private DFDModelRepository dfdModelRepository;
    private StorageDataRepository storageDataRepository;
    private WorkingAreaRepository workingAreaRepository;
    private DFDModelAnalysisService dfdModelAnalysisService;
    private ProjectRepository projectRepository;
    private JwtProvider jwtProvider;
    private MLRepository mlRepository;
    private CVEService cveService;

    @Autowired
    public ModelApiController(DFDModelRepository dfdModelRepository,
                              StorageDataRepository storageDataRepository,
                              WorkingAreaRepository workingAreaRepository,
                              ProjectRepository projectRepository,
                              DFDModelAnalysisService dfdModelAnalysisService,
                              MLRepository mlRepository,
                              CVEService cveService,
                              JwtProvider jwtProvider) {
        this.dfdModelRepository = dfdModelRepository;
        this.storageDataRepository = storageDataRepository;
        this.workingAreaRepository = workingAreaRepository;
        this.projectRepository = projectRepository;
        this.dfdModelAnalysisService = dfdModelAnalysisService;
        this.mlRepository = mlRepository;
        this.cveService = cveService;
        this.jwtProvider = jwtProvider;
    }


    @Override
    @CrossOrigin
    public ResponseEntity<Void> saveDFDModel(@RequestHeader(value="Authorization", required=true) String authorizationHeader,
                                             @ApiParam(value = "Threat Model") @Valid @RequestBody BaseDFDModelResource dfdModelResource) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorizationHeader);
        // 1. Check if the model is valid and user is allowed to save it
        if (dfdModelResource == null
                || ModelValidationUtil.baseDFDModelIsInvalid(dfdModelResource)
                || modelDataIncludesProjectIDUserIsNotPartOf(dfdModelResource, username)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 2. Create DFD Model to be stored
        StoredDFDModelResource linkedModel = DFDModelResourceToEntityConverter.convert(dfdModelResource);
        // 3. Create the Metadata of the DFD Model to be stored
        ModelStorageData storageData = BaseStorageDataResourceToEntityConverter.convert(dfdModelResource.getStorageData(), username, linkedModel.getModelID());

        // 4. Link the model to a project if applicable
        if(storageData.getProjectID() != null) {
            // We don't pass the username, because we already checked earlier if the user is allowed to link the model
            linkModelToProject(storageData.getProjectID(), storageData.getModelID());
        }

        // 5. Save both the DFDModel and its Metadata
        dfdModelRepository.save(linkedModel);
        storageDataRepository.save(storageData);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private void linkModelToProject(String projectID, String modelID) {
        Project project = projectRepository.findByProjectID(projectID).orElse(null);
        if (project != null) {
            List<String> linkedModels = project.getModels();
            linkedModels.add(modelID);
            project.setModels(linkedModels);
            updateProjectInRepository(project);
        }
    }

    private boolean modelDataIncludesProjectIDUserIsNotPartOf(BaseDFDModelResource dfdModelResource, String username) {
        if (dfdModelResource.getStorageData().getProjectID() == null
                || dfdModelResource.getStorageData().getProjectID().equals("")) {
            return false;
        }
        Project linkedProject = projectRepository.findByProjectID(dfdModelResource.getStorageData().getProjectID()).orElse(null);
        return linkedProject.getCollaborators() == null ||
                !linkedProject.getCollaborators().contains(username);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> deleteDfdModel(String modelID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        // 1. Load all the data available to a user
        List<ModelStorageData> storageDataList = storageDataRepository.findAllByUsername(username).orElse(null);
        if (storageDataList == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 2. Find the specific one the user wants to delete
        ModelStorageData storageData = FindUtil.findStorageDataByID(modelID, storageDataList);
        if (storageData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 3. If applicable, find a project the model is linked to and unlink it
        findAndUnlinkModelFromProjects(storageData.getProjectID(), storageData.getModelID());
        // 4. Delete the model
        dfdModelRepository.deleteByModelID(storageData.getModelID());
        // 5. Delete the storageData
        storageDataRepository.deleteByModelID(storageData.getModelID());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void findAndUnlinkModelFromProjects(String projectID, String modelID) {
        if (projectID != null) {
            Project project = projectRepository.findByProjectID(projectID).orElse(null);
            if (project != null && project.getModels() != null) {
                List<String> linkedModels = project.getModels();
                linkedModels.removeIf(model -> model.equals(modelID));
                // Updated the project with the updated linked model list
                project.setModels(linkedModels);
                // Update the project in the repository
               updateProjectInRepository(project);
            }
        }
    }

    private void updateProjectInRepository(Project project) {
        // Delete the old project
        projectRepository.deleteByProjectID(project.getProjectID());
        // Save updated project
        projectRepository.save(project);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<AnalysisResultResource>  analyzeThreatModel(String authorization, AnalysisDFDModelResource dfdModel) {
        if (dfdModel == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Because the analysis model will be stored, it needs an ID in case the same model is analyzed later again
        if (dfdModel.getModelID().equals("")) {
            dfdModel.setModelID(UUID.randomUUID().toString());
        }
        // We extract the username and find the old working area data
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        WorkingArea oldWorkingArea = workingAreaRepository.findFirstByUsername(username).orElse(null);
        // And delete the old one, so only one is cached at all times
        workingAreaRepository.deleteAllByUsername(username);
        List<AppliedThreatResource> foundThreats;

        // If the stored model has a different model ID than the new model, we delete the old working area and do a fresh analysis
        if (oldWorkingArea == null || !oldWorkingArea.getCurrentModel().getModelID().equals(dfdModel.getModelID())) {
            foundThreats = findThreatResourcesAndConvertToAppliedThreats(dfdModel);
        } else {
            // Otherwise, we extract the elements which got changed, analyze them, and combine them with the old ones (distinct threats)
            AnalysisDFDModelResource modelWithElementsThatNeedToBeAnalyzed
                    = AnalysisUtil.collectElementsContainedInDFDModelWhichNeedAnalysis(dfdModel, oldWorkingArea.getCurrentModel());
            foundThreats =
                    analyzeDFDModelForThreats(modelWithElementsThatNeedToBeAnalyzed, oldWorkingArea.getFoundThreats());
            AnalysisUtil.checkIfElementsWereRemovedAndRemoveApplyingThreats(dfdModel, oldWorkingArea.getCurrentModel(), foundThreats);
        }

        // Then we load the vulnerability data...
        List<CVEResource> cveItems = new ArrayList<>();
        if (CheckUtil.dfdModelIncludesCPE(dfdModel)) {
            cveItems = loadCVEItems(dfdModel);
        }
        // Save the new working area...
        createAndSaveNewWorkingAreaState(username, dfdModel, foundThreats);
        // And return the new Data
        return new ResponseEntity<>(
                new AnalysisResultResource()
                .modelID(dfdModel.getModelID())
                .threats(foundThreats)
                .vulnerabilities(cveItems), HttpStatus.OK);
    }

    private List<AppliedThreatResource> findThreatResourcesAndConvertToAppliedThreats(AnalysisDFDModelResource model) {
        List<ThreatResource> foundThreats = dfdModelAnalysisService.analyzeThreatModel(model);
        return ThreatResourceToAppliedThreatConverter.convertThreatResourcesToAppliedThreats(foundThreats);
    }

    private List<AppliedThreatResource> analyzeDFDModelForThreats(AnalysisDFDModelResource model,
                                                                  List<AppliedThreatResource> oldThreats) {
        List<AppliedThreatResource> appliedThreats = findThreatResourcesAndConvertToAppliedThreats(model);
        return DeleteUtil.deleteDuplicateThreatsFromThreatList(appliedThreats, oldThreats);
    }

    private List<CVEResource> loadCVEItems(AnalysisDFDModelResource dfdModel) {
        List<CVEResource> foundCVES = new ArrayList<>();
        List<CVEResource> cves = cveService.loadAllCVES();
        CVEUtil.extractCPEsFromAnalysisDFDModel(dfdModel)
                .forEach(cpe -> foundCVES.addAll(cveService.findCVEDataAffectingCPE(cpe, cves)));
        return foundCVES;
    }

    private void createAndSaveNewWorkingAreaState(String username,
                                                  AnalysisDFDModelResource dfdModel,
                                                  List<AppliedThreatResource> newThreats) {
        WorkingArea workingArea = new WorkingArea(username, dfdModel, newThreats);
        workingAreaRepository.save(workingArea);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> updateThreatData(String authorization, AppliedThreatResource body) {
        if (body == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // First, we need the current working area to get the found threats
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        WorkingArea currentWorkingArea = workingAreaRepository.findFirstByUsername(username).orElse(null);

        if (currentWorkingArea != null && currentWorkingArea.getFoundThreats() != null){
            List<AppliedThreatResource> applyingThreats = currentWorkingArea.getFoundThreats();
            // If the threat from the frontend does not exist in the list for some reason, something went really wrong
            if (FindUtil.threatExistsInListOfThreats(applyingThreats, body.getThreatID())) {
                // Because the body is an update, we can delete the old threat...
                DeleteUtil.deleteThreatFromThreatList(applyingThreats, body.getThreatID());
                // ...the old working area...
                workingAreaRepository.deleteAllByUsername(username);
                // ...and add the new one
                applyingThreats.add(body);
                currentWorkingArea.setFoundThreats(applyingThreats);
                workingAreaRepository.save(currentWorkingArea);
                // We also need to update the MLData
                updateMLDataSets(body, currentWorkingArea.getCurrentModel());
            } else {
                return new ResponseEntity<>(HttpStatus.GONE);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void updateMLDataSets(AppliedThreatResource body, AnalysisDFDModelResource model) {
        ThreatDataSetResource mlDataSet = mlRepository.findById(body.getThreatID()).orElse(null);
        // If an dataset containing the treat already exists and has an applicable status set..
        if (mlDataSet != null && body.getApplicable() != null) {
            // ...we need to update the old dataset
            updateExistingMLDataSet(body.getThreatID(), body.getApplicable(), mlDataSet);
        } else if (mlDataSet == null && body.getApplicable() != null && body.getApplicable() != ApplicableStateResource.NOT_SELECTED) {
            // If no matching dataset exists already, we create a new one
            createNewDataSet(body, model);
        }
    }

    private void updateExistingMLDataSet(String threatID,
                                         ApplicableStateResource applicable,
                                         ThreatDataSetResource mlDataSet) {
        mlRepository.deleteById(threatID);
        if (applicable != ApplicableStateResource.NOT_SELECTED) {
            mlDataSet.setApplicable(applicable == ApplicableStateResource.APPLICABLE);
            mlRepository.save(mlDataSet);
        }
    }

    private void createNewDataSet(AppliedThreatResource threat,
                                  AnalysisDFDModelResource model) {
        AnalysisDataFlowResource affectedDataFlow =
                model.getDataFlows()
                        .stream()
                        .filter(dataFlow -> threat.getAffectedElements().contains(dataFlow.getId()))
                        .findAny().orElse(null);
        // If nothing went wrong...
        if (affectedDataFlow != null) {
            // ...we create a new dataset and save it.
            ThreatDataSetResource mlDataSet = new ThreatDataSetResource().applicable(threat.getApplicable() == ApplicableStateResource.APPLICABLE);
            mlDataSet.setStartItem(MLUtil.findMLElementItem(affectedDataFlow.getStartElement(), model));
            mlDataSet.setEndItem(MLUtil.findMLElementItem(affectedDataFlow.getEndElement(), model));
            mlDataSet.setDataFlow(MLUtil.createMLElementItemFromDataFlow(affectedDataFlow));
            mlDataSet.setId(threat.getThreatID());
            mlDataSet.setThreat(AppliedThreatToThreatResourceConverter.convert(threat));
            mlRepository.save(mlDataSet);
        }
    }

    @Override
    @CrossOrigin
    public ResponseEntity<StoredDFDModelResource> loadModel(String modelID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        // 1. Find all model meta data of models belonging to a user
        ModelStorageData modelStorageData = getModelStorageDataUserIsRequesting(modelID, username);
        if (modelStorageData == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            StoredDFDModelResource dfdModel = dfdModelRepository.findByModelID(modelStorageData.getModelID()).orElse(null);
            return dfdModel == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(dfdModel, HttpStatus.OK);
        }
    }

    private ModelStorageData getModelStorageDataUserIsRequesting(String modelID, String username) {
        ModelStorageData modelStorageData = storageDataRepository.findByModelID(modelID).orElse(null);
        if (modelStorageData == null) {
            return null;
        } else if (modelStorageData.getUsername().equals(username)) {
            return modelStorageData;
        } else {
            List<Project> projects = projectRepository.findAllByCollaboratorsContaining(username).orElse(new ArrayList<>());
            if (ProjectValidationUtil.modelIDExistsInAProjectUserIsPartOf(projects, modelID)) {
                return modelStorageData;
            }
            return null;
        }
    }

}
