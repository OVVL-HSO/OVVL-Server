package com.tam.api.controller;

import com.tam.api.ProjectApi;
import com.tam.converter.ProjectEntityToProjectResourceConverter;
import com.tam.model.*;
import com.tam.repositories.InvitationRepository;
import com.tam.repositories.ProjectRepository;
import com.tam.repositories.StorageDataRepository;
import com.tam.security.Jwt.JwtProvider;
import com.tam.services.projects.ProjectInviteService;
import com.tam.services.projects.ProjectService;
import com.tam.utils.MapUtil;
import com.tam.utils.ProjectUtil;
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
import java.util.stream.Collectors;

@EnableAutoConfiguration
@RestController
public class ProjectApiController implements ProjectApi {

    private JwtProvider jwtProvider;
    private ProjectService projectService;
    private ProjectInviteService projectInviteService;

    @Autowired
    public ProjectApiController(JwtProvider jwtProvider,
                                ProjectInviteService projectInviteService,
                                ProjectService projectService) {
        this.jwtProvider = jwtProvider;
        this.projectService = projectService;
        this.projectInviteService = projectInviteService;
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> createProject(String authorization, BaseProjectResource baseProjectData) {
        String owner = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.createAndValidateProject(owner, baseProjectData);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> inviteUser(String authorization, InviteRequestResource inviteData) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectInviteService.inviteUserToProject(username, inviteData);
    }


    @Override
    @CrossOrigin
    public ResponseEntity<Void> acceptInvitation(String invitationID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectInviteService.acceptInvitationToProject(invitationID, username);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> declineInvitation(String invitationID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectInviteService.declineInvitationToProject(username, invitationID);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<List<ProjectResource>> loadProjects(String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.loadExistingProjects(username);
    }


    @Override
    @CrossOrigin
    public ResponseEntity<Void> deleteProject(String projectID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.deleteProjectAndAllCorrespondingData(projectID, username);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> linkModel(String authorization,
                                          DFDProjectLinkResource dfdProjectLink) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.linkModelToProject(username, dfdProjectLink);
    }


    @Override
    @CrossOrigin
    public ResponseEntity<Void> unlinkModel(String authorization, DFDProjectLinkResource dfdProjectLink) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.unlinkModelFromProject(username, dfdProjectLink);
    }

    @Override
    @CrossOrigin
    public ResponseEntity<Void> leaveProject(String projectID, String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        return projectService.leaveProject(projectID, username);
    }
}
