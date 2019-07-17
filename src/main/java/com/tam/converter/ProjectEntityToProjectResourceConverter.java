package com.tam.converter;

import com.tam.model.Project;
import com.tam.model.ProjectResource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProjectEntityToProjectResourceConverter {

  public static List<ProjectResource> convertProjectEntitiesToResources(List<Project> projects) {
    return projects.stream().map(ProjectEntityToProjectResourceConverter::convert).collect(Collectors.toList());
  }

  public static ProjectResource convert(Project project) {
    return new ProjectResource()
            .projectID(project.getProjectID())
            .collaborators(project.getCollaborators())
            .owner(project.getOwner())
            .invites(project.getInvites() != null ? project.getInvites() : new ArrayList<>())
            .description(project.getDescription())
            .title(project.getTitle())
            .creationDate(project.getCreationDate())
            .models(project.getModels() != null ? project.getModels() : new ArrayList<>());
  }

}
