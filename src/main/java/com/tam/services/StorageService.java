package com.tam.services;

import com.tam.model.InvitationResource;
import com.tam.model.ModelStorageData;
import com.tam.model.Project;
import com.tam.repositories.InvitationRepository;
import com.tam.repositories.ProjectRepository;
import com.tam.repositories.StorageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {

    private ProjectRepository projectRepository;
    private StorageDataRepository storageDataRepository;
    private InvitationRepository invitationRepository;

    @Autowired
    public StorageService(ProjectRepository projectRepository,
                          InvitationRepository invitationRepository,
                          StorageDataRepository storageDataRepository) {
        this.projectRepository = projectRepository;
        this.storageDataRepository = storageDataRepository;
        this.invitationRepository = invitationRepository;
    }

    public void saveMultipleInvitations(List<InvitationResource> invitations) {
        invitationRepository.saveAll(invitations);
    }

    public void updateProject(Project project) {
        deleteProject(project.getProjectID());
        saveProject(project);
    }

    private void deleteProject(String projectID) {
        projectRepository.deleteByProjectID(projectID);
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public void updateModelStorageData(ModelStorageData storageData) {
        deleteModelStorageData(storageData.getModelID());
        saveModelStorageData(storageData);
    }

    private void saveModelStorageData(ModelStorageData storageData) {
        storageDataRepository.save(storageData);
    }

    private void deleteModelStorageData(String modelID) {
        storageDataRepository.deleteByModelID(modelID);
    }

    public List<Project> findProjectsByNameIncludedInTheProjectMembers(String username) {
        return projectRepository.findAllByCollaboratorsContaining(username).orElse(new ArrayList<>());
    }

    public Project findProjectByID(String projectID) {
        return projectRepository.findByProjectID(projectID).orElse(null);
    }

    public void deleteAllInvitesByProjectID(String projectID) {
        projectRepository.deleteByProjectID(projectID);
    }

    public void deleteProjectByID(String projectID) {
        projectRepository.deleteByProjectID(projectID);
    }

    public ModelStorageData findModelByID(String modelID) {
        return storageDataRepository.findByModelID(modelID).orElse(null);
    }

    public List<Project> findProjectsWhichIncludeAModeLID(String modelID) {
        return projectRepository.findAllByModelsContaining(modelID).orElse(null);
    }

    public List<InvitationResource> findAllInvitesByUsername(String username) {
        return invitationRepository.findAllByUsername(username).orElse(new ArrayList<>());
    }

    public void saveInvitation(InvitationResource invitation) {
        invitationRepository.save(invitation);
    }

    public InvitationResource findInvitationByID(String invitationID) {
        return invitationRepository.findByInvitationID(invitationID).orElse(null);
    }

    public void deleteInvitationByID(String invitationID) {
        invitationRepository.deleteByInvitationID(invitationID);
    }
}
