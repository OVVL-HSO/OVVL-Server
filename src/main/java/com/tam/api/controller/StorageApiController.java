package com.tam.api.controller;

import com.google.common.collect.Lists;
import com.tam.api.StorageApi;
import com.tam.converter.ModelStorageDataEntityToResourceConverter;
import com.tam.converter.ProjectEntityToProjectResourceConverter;
import com.tam.model.*;
import com.tam.repositories.InvitationRepository;
import com.tam.repositories.ProjectRepository;
import com.tam.repositories.StorageDataRepository;
import com.tam.security.Jwt.JwtProvider;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@EnableAutoConfiguration
@RestController
public class StorageApiController implements StorageApi {

    private StorageDataRepository storageDataRepository;
    private JwtProvider jwtProvider;
    private ProjectRepository projectRepository;
    private InvitationRepository invitationRepository;

    @Autowired
    public StorageApiController(StorageDataRepository storageDataRepository,
                                JwtProvider jwtProvider,
                                InvitationRepository invitationRepository,
                                ProjectRepository projectRepository) {
        this.storageDataRepository = storageDataRepository;
        this.jwtProvider = jwtProvider;
        this.invitationRepository = invitationRepository;
        this.projectRepository = projectRepository;
    }

    @Override
    @CrossOrigin
    public ResponseEntity<List<ModelStorageDataResource>> findModels(@ApiParam(value = "Authorization header" ,required=true) @RequestHeader(value="Authorization") String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        List<ModelStorageDataResource> relevantStorageData = loadRelevantStorageData(username);
        return new ResponseEntity<>(Lists.reverse(relevantStorageData), HttpStatus.OK);
    }

    private List<ModelStorageDataResource> loadRelevantStorageData(String username) {
        final List<ModelStorageData> storageData = storageDataRepository.findAllByUsername(username).orElse(new ArrayList<>());
        return ModelStorageDataEntityToResourceConverter.convertStorageDataEntitiesToResources(storageData);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<StoredWorkResource> findStoredWork(String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return new ResponseEntity<>(getWorkUserHasStored(username), HttpStatus.OK);
    }

    private List<ModelStorageDataResource> getModelStorageDataBelongingToUserAndFromProjectsUserIsPartOf(List<ProjectResource> projects, String username) {
        // 1. Get the ModelStorageData belonging to a user and map it to its returned DataType
        List<ModelStorageDataResource> modelStorageData = ModelStorageDataEntityToResourceConverter
                .convertStorageDataEntitiesToResources(storageDataRepository.findAllByUsername(username).orElse(new ArrayList<>()));
        // 2. Add the ModelStorageData made available to the user through their projects
        modelStorageData.addAll(getModelsLinkedToAProjectUserIsPartOf(projects, username));
        return modelStorageData;
    }

    private List<ModelStorageDataResource> getModelsLinkedToAProjectUserIsPartOf(List<ProjectResource> projects, String username) {
        return projects.stream()
                // 1. make a list of lists containing the models linked to a project
                .map(ProjectResource::getModels)
                // 2. Turn that list of list into a single list
                .flatMap(List::stream)
                // 3. Map the list of modelIds to ModelStorageData stored in the repository
                .map(modelID -> storageDataRepository.findByModelID(modelID).orElse(null))
                // 4. Filter out all null values
                .filter(Objects::nonNull)
                // 5. Filter out all models which the user created -> we already got that earlier
                .filter(modelStorageData -> !modelStorageData.getUsername().equals(username))
                // 6. Convert the ModelStorage datatype to the one that will be returned
                .map(ModelStorageDataEntityToResourceConverter::convert)
                // 6. Collect the stream into a list.
                .collect(Collectors.toList());
    }

    private List<Project> getAllProjectsUserIsPartOf(String username) {
        return projectRepository.findAllByCollaboratorsContaining(username).orElse(new ArrayList<>());
    }

    private StoredWorkResource getWorkUserHasStored(String username) {
        // 1. Get the projects the user is linked to and convert them to the datatype returned later
        List<ProjectResource> projects = ProjectEntityToProjectResourceConverter
                .convertProjectEntitiesToResources(getAllProjectsUserIsPartOf(username));
        // 2. Get the storageData the user is linked to
        List<ModelStorageDataResource> storageData = getModelStorageDataBelongingToUserAndFromProjectsUserIsPartOf(projects, username);
        return new StoredWorkResource().models(storageData).projects(projects);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<List<InvitationResource>> getInvites(String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        List<InvitationResource> invitations = invitationRepository.findAllByUsername(username).orElse(new ArrayList<>());
        return new ResponseEntity<>(invitations, HttpStatus.OK);
    }

}
