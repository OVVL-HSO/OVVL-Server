package com.tam.utils;

import com.tam.model.BaseProjectResource;
import com.tam.model.Project;
import com.tam.model.ModelStorageData;
import com.tam.utils.validation.ProjectValidationUtil;

import java.util.*;

public class ProjectUtil {
  public static Project mapBaseProjectToProject(BaseProjectResource baseProject, String owner) {
    return Project.builder()
            .title(baseProject.getTitle())
            .description(baseProject.getDescription())
            .invites(baseProject.getInvites())
            .collaborators(new ArrayList<>(Collections.singletonList(owner)))
            .models(baseProject.getModels())
            .creationDate(GeneralUtil.getCurrentDateAsISOString())
            .owner(owner)
            .projectID(UUID.randomUUID().toString())
            .build();
  }

  public static boolean modelCanBeLinkedToProject(String username, ModelStorageData storageData, Project project, List<Project> projectsModelAlreadyIsLinkedTo) {
      return projectsModelAlreadyIsLinkedTo.size() == 0
              && project != null
              && storageData != null
              && ProjectValidationUtil.userIsPartOfProjectOrOwner(project, username)
              && ProjectValidationUtil.modelBelongsToUser(storageData, username);
  }

  private static boolean ownerInvitedThemselves(BaseProjectResource project, String owner) {
    return project.getInvites().stream().anyMatch(invite -> invite.equals(owner));
  }

  private static boolean usersAreInvitedMultipleTimes(BaseProjectResource project) {
    return project.getInvites().stream().anyMatch(invite -> Collections.frequency(project.getInvites(), invite) > 1);
  }

  public static boolean modelCanBeUnlinkedFromProject(String username, Project project, ModelStorageData modelStorageData) {
    return project != null
            && modelStorageData != null
            && modelAndProjectAreLinked(project.getModels(), modelStorageData.getModelID(), project.getProjectID(), modelStorageData.getProjectID())
            && userIsAllowedToUnlinkModel(project.getOwner(), project.getCollaborators(), username, modelStorageData.getUsername());
  }

  private static boolean userIsAllowedToUnlinkModel(String projectOwner, List<String> collaborators, String username, String modelCreator) {
    return collaborators.contains(username) && (username.equals(projectOwner) || username.equals(modelCreator));
  }

  private static boolean modelAndProjectAreLinked(List<String> modelsLinkedToProject, String modelID, String projectID, String projectIDStoredInModel) {
    return modelID != null
            && modelsLinkedToProject != null
            && projectIDStoredInModel != null
            && modelsLinkedToProject.contains(modelID)
            && projectIDStoredInModel.equals(projectID);
  }

    public static boolean projectCanNotBeCreated(BaseProjectResource baseProjectData, String owner) {
        return baseProjectData == null
                || baseProjectData.getDescription() == null
                || baseProjectData.getTitle() == null
                || ProjectUtil.ownerInvitedThemselves(baseProjectData, owner)
                || ProjectUtil.usersAreInvitedMultipleTimes(baseProjectData);
    }
}
