package com.tam.services.projects;

import com.tam.model.InvitationResource;
import com.tam.model.InviteRequestResource;
import com.tam.model.Project;
import com.tam.services.StorageService;
import com.tam.utils.MapUtil;
import com.tam.utils.validation.ProjectValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectInviteService {
    private StorageService storageService;

    @Autowired
    public ProjectInviteService(StorageService storageService) {
        this.storageService = storageService;
    }

    public ResponseEntity<Void> inviteUserToProject(String username, InviteRequestResource inviteData) {
        return (inviteData.getProjectID() == null || inviteData.getUsername() == null)
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : handleInvitation(inviteData, username);
        }

    private ResponseEntity<Void> handleInvitation(InviteRequestResource inviteData, String username) {
        Project project = storageService.findProjectByID(inviteData.getProjectID());
        List<InvitationResource> invites = storageService.findAllInvitesByUsername(inviteData.getUsername());
        if (ProjectValidationUtil.inviteToProjectIsPossible(project, inviteData, username, invites)) {
            createInvitation(inviteData.getUsername(), project);
            addUsernameToExistingInvitesInProject(project, inviteData.getUsername());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void createInvitation(String username, Project project) {
        InvitationResource invitation = MapUtil.mapProjectAndUsernameToInvitation(username, project);
        storageService.saveInvitation(invitation);
    }

    private void addUsernameToExistingInvitesInProject(Project project, String invite) {
        List<String> existingInvites = project.getInvites();
        existingInvites.add(invite);
        project.setInvites(existingInvites);
        storageService.updateProject(project);
    }

    public ResponseEntity<Void> acceptInvitationToProject(String invitationID, String username) {
        return invitationID == null
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : handleInvitationAcception(invitationID, username);
    }

    private ResponseEntity<Void> handleInvitationAcception(String invitationID, String username) {
        Project project = findProjectFromInvitationID(invitationID);
        if (project == null || !ProjectValidationUtil.userCanJoinProject(username, project)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        addUserToCollaboratorsAndRemoveFromInvited(project, username);
        storageService.deleteInvitationByID(invitationID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void addUserToCollaboratorsAndRemoveFromInvited(Project project, String username) {
        List<String> invites = project.getInvites().stream().filter(invite -> !invite.equals(username)).collect(Collectors.toList());
        List<String> collaborators = project.getCollaborators();
        collaborators.add(username);
        project.setInvites(invites);
        project.setCollaborators(collaborators);
        storageService.updateProject(project);
    }

    public ResponseEntity<Void> declineInvitationToProject(String username, String invitationID) {
        return (invitationID == null || username == null)
                ? new ResponseEntity<>(HttpStatus.BAD_REQUEST)
                : handleInvitationDecline(invitationID, username);
    }

    private ResponseEntity<Void> handleInvitationDecline(String invitationID, String username) {
        Project project = findProjectFromInvitationID(invitationID);
        if (project == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        removeUserFromInvitedList(project, username);
        storageService.deleteInvitationByID(invitationID);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Project findProjectFromInvitationID(String invitationID) {
        InvitationResource invitation = storageService.findInvitationByID(invitationID);
        if (invitation != null) {
            return storageService.findProjectByID(invitation.getProjectID());
        }
        return null;
    }

    private void removeUserFromInvitedList(Project project, String username) {
        List<String> invitations = project.getInvites();
        invitations.removeIf(invitation -> invitation.equals(username));
        project.setInvites(invitations);
        storageService.updateProject(project);
    }
}
