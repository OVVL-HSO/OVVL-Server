package com.tam.utils.validation;

import com.tam.model.InvitationResource;
import com.tam.model.InviteRequestResource;
import com.tam.model.Project;
import com.tam.model.ModelStorageData;
import com.tam.utils.CheckUtil;

import java.util.List;
import java.util.stream.Collectors;

public class ProjectValidationUtil {

  public static boolean userIsPartOfProjectOrOwner(Project project, String username) {
    return project.getOwner().equals(username)
      || project.getCollaborators().stream().anyMatch(collaborator -> collaborator.equals(username));
  }

  private static boolean userIsAllowedToBeInvited(String username, Project project, String invite) {
    return project.getOwner().equals(username)
      && !ownerIsInvited(username, invite)
      && !usersAreAlreadyInvited(project.getInvites(), invite)
      && !usersAreNotAlreadyCollaborators(project.getCollaborators(), invite);
  }

  private static boolean ownerIsInvited(String owner, String invite) {
    return invite.equals(owner);
  }

  private static boolean usersAreNotAlreadyCollaborators(List<String> collaborators, String invite) {
    return CheckUtil.stringExistsInAListOfStrings(collaborators, invite);
  }

  private static boolean usersAreAlreadyInvited(List<String> existingInvites, String invite) {
    return CheckUtil.stringExistsInAListOfStrings(existingInvites, invite);
  }

  public static boolean userCanJoinProject(String username, Project project) {
    return !project.getOwner().equals(username)
      && !userIsAlreadyPartOfProject(project.getCollaborators(), username)
      && userIsInvited(project.getInvites(), username);
  }

  private static boolean userIsInvited(List<String> invites, String username) {
    return invites.stream().anyMatch(invite -> invite.equals(username));
  }

  private static boolean userIsAlreadyPartOfProject(List<String> collaborators, String username) {
    return collaborators.stream().anyMatch(collaborator -> collaborator.equals(username));
  }

  public static boolean modelBelongsToUser(ModelStorageData storageData, String username) {
    return storageData.getUsername().equals(username);
  }

  private static boolean inviteAlreadyExists(List<InvitationResource> invites, String projectID) {
      return invites.stream().anyMatch(invite -> invite.getProjectID().equals(projectID));
  }

  public static boolean inviteToProjectIsPossible(Project project,
                                                  InviteRequestResource inviteData,
                                                  String username,
                                                  List<InvitationResource> invites) {
    return project != null
            && !ProjectValidationUtil.inviteAlreadyExists(invites, project.getProjectID())
            && ProjectValidationUtil.userIsAllowedToBeInvited(username, project, inviteData.getUsername());
  }

  public static boolean modelIDExistsInAProjectUserIsPartOf(List<Project> projectsUserIsPartOf, String modelID) {
    return projectsUserIsPartOf.stream()
            .map(Project::getModels)
            .flatMap(List::stream)
            .collect(Collectors.toList())
            .contains(modelID);
  }
}
