package com.tam.services.projects;

import com.tam.converter.ProjectEntityToProjectResourceConverter;
import com.tam.model.*;
import com.tam.services.StorageService;
import com.tam.utils.MapUtil;
import com.tam.utils.ProjectUtil;
import com.tam.utils.validation.ProjectValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private StorageService storageService;

    @Autowired
    public ProjectService(StorageService storageService) {
        this.storageService = storageService;
    }

    public ResponseEntity<Void> createAndValidateProject(String owner, BaseProjectResource baseProjectData) {
       return ProjectUtil.projectCanNotBeCreated(baseProjectData, owner)
               ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
               : handleProjectCreation(baseProjectData, owner);
    }

    private ResponseEntity<Void> handleProjectCreation(BaseProjectResource baseProjectData, String owner) {
        Project project = ProjectUtil.mapBaseProjectToProject(baseProjectData, owner);
        createInvitesIfProjectIncludesAny(project);
        storageService.saveProject(project);
        if (baseProjectData.getModels() != null) {
            linkAndUnlinkModelsToAndFromProjects(baseProjectData.getModels(), project.getProjectID());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void createInvitesIfProjectIncludesAny(Project project) {
        List<InvitationResource> invitations = MapUtil.mapProjectToAListOfInvitations(project);
        storageService.saveMultipleInvitations(invitations);
    }

    private void linkAndUnlinkModelsToAndFromProjects(List<String> models, String projectID) {
        models.stream().map(model -> storageService.findModelByID(model))
                .forEach(storageData -> linkAndUnlinkASingleModelToAndFromProjects(storageData, projectID));
    }

    private void linkAndUnlinkASingleModelToAndFromProjects(ModelStorageData modelStorageData, String projectID) {
        if (modelStorageData != null) {
            modelStorageData.setProjectID(projectID);
            storageService.updateModelStorageData(modelStorageData);
        }
    }

    public ResponseEntity<List<ProjectResource>> loadExistingProjects(String username) {
        List<Project> projects = storageService.findProjectsByNameIncludedInTheProjectMembers(username);
        List<ProjectResource> projectResources = ProjectEntityToProjectResourceConverter.convertProjectEntitiesToResources(projects);
        return new ResponseEntity<>(projectResources, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteProjectAndAllCorrespondingData(String projectID, String username) {
        return projectID == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : deleteProject(projectID, username);
    }

    private ResponseEntity<Void> deleteProject(String projectID, String username) {
        Project project = storageService.findProjectByID(projectID);
        // Make sure the user sending the delete request is the project owner
        if (project == null || !project.getOwner().equals(username)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        linkAndUnlinkModelsToAndFromProjects(project.getModels(), "");
        storageService.deleteAllInvitesByProjectID(projectID);
        storageService.deleteProjectByID(projectID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Void> linkModelToProject(String username, DFDProjectLinkResource dfdProjectLink) {
        return (dfdProjectLink.getModelID() == null || dfdProjectLink.getProjectID() == null)
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : handleProjectModelLink(dfdProjectLink, username);
        }

    private ResponseEntity<Void> handleProjectModelLink(DFDProjectLinkResource dfdProjectLink, String username) {
        Project project = storageService.findProjectByID(dfdProjectLink.getProjectID());
        ModelStorageData modelStorageData = storageService.findModelByID(dfdProjectLink.getModelID());
        List<Project> projectsModelAlreadyIsLinkedTo = storageService.findProjectsWhichIncludeAModeLID(dfdProjectLink.getModelID());
        if (ProjectUtil.modelCanBeLinkedToProject(username, modelStorageData, project, projectsModelAlreadyIsLinkedTo) ) {
            addModelToExistingModelsInProject(project, dfdProjectLink.getModelID());
            addProjectIDToModel(modelStorageData, dfdProjectLink.getProjectID());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private void addProjectIDToModel(ModelStorageData modelStorageData, String projectID) {
        modelStorageData.setProjectID(projectID);
        storageService.updateModelStorageData(modelStorageData);
    }

    private void addModelToExistingModelsInProject(Project project, String newModelID) {
        List<String> modelsInProject = project.getModels();
        modelsInProject.add(newModelID);
        project.setModels(modelsInProject);
        storageService.updateProject(project);
    }

    public ResponseEntity<Void> unlinkModelFromProject(String username, DFDProjectLinkResource dfdProjectLink) {
        return (dfdProjectLink.getModelID() == null || dfdProjectLink.getProjectID() == null)
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : handleProjectModelUnlink(username, dfdProjectLink);

    }

    private ResponseEntity<Void> handleProjectModelUnlink(String username, DFDProjectLinkResource dfdProjectLink) {
        Project project = storageService.findProjectByID(dfdProjectLink.getProjectID());
        ModelStorageData modelStorageData = storageService.findModelByID(dfdProjectLink.getModelID());
        if (ProjectUtil.modelCanBeUnlinkedFromProject(username, project, modelStorageData)) {
            linkAndUnlinkASingleModelToAndFromProjects(modelStorageData, "");
            unlinkModelFromProject(project, modelStorageData.getModelID());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private void unlinkModelFromProject(Project project, String modelID) {
        List<String> modelsLinkedToProject = project.getModels();
        modelsLinkedToProject.removeIf(model -> model.equals(modelID));
        project.setModels(modelsLinkedToProject);
        storageService.updateProject(project);
    }

    public ResponseEntity<Void> leaveProject(String projectID, String username) {
        return projectID == null ? new ResponseEntity<>(HttpStatus.BAD_REQUEST) : handleProjectLeaving(username, projectID);
    }

    private ResponseEntity<Void> handleProjectLeaving(String username, String projectID) {
        Project project = storageService.findProjectByID(projectID);
        if (project == null || project.getOwner().equals(username) || !ProjectValidationUtil.userIsPartOfProjectOrOwner(project, username)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            // 1. Remove from collaborators and save
            removeUserFromProjectCollaborators(project, username);
            // 2. Remove all models from project belonging to the user
            removeAllModelsFromProjectLinkedToTheUserLeaving(project, username);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    private void removeAllModelsFromProjectLinkedToTheUserLeaving(Project project, String username) {
        List<String> idsOfModelsLinkedToProject = project.getModels()
                .stream()
                .map(id -> {
                    // 1. Find the model linked in the project
                    ModelStorageData storageData = storageService.findModelByID(id);
                    // 2. If the model exists and the user is its owner, unlink it and don't return the id again
                    if (storageData != null && storageData.getUsername().equals(username)) {
                        linkAndUnlinkASingleModelToAndFromProjects(storageData, "");
                        return null;
                    // 3. If the user is not the owner but the model exists, just return the id (because the model will still be linked)
                    } else if (storageData != null) {
                        return id;
                    }
                    // 4. If the model does not exist, something went really wrong and it's good to unlink the model now.
                    else return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
        project.setModels(idsOfModelsLinkedToProject);
        storageService.updateProject(project);
    }

    private void removeUserFromProjectCollaborators(Project project, String username) {
        List<String> projectCollaborators = project.getCollaborators();
        projectCollaborators.removeIf(collaborator -> collaborator.equals(username));
        project.setCollaborators(projectCollaborators);
        storageService.updateProject(project);
    }
}
