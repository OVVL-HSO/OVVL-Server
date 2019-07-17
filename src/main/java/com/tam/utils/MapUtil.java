package com.tam.utils;

import com.tam.model.InvitationResource;
import com.tam.model.Project;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MapUtil {
      public static InvitationResource mapProjectAndUsernameToInvitation(String username, Project project) {
        return new InvitationResource()
                .invitationID(UUID.randomUUID().toString())
                .title(project.getTitle())
                .description(project.getDescription())
                .owner(project.getOwner())
                .projectID(project.getProjectID())
                .username(username)
                .numberOfCollaborators(project.getCollaborators().size())
                .invitedAt(GeneralUtil.getCurrentDateAsISOString());
    }

    public static List<InvitationResource> mapProjectToAListOfInvitations(Project project) {
        return project.getInvites().stream().map(invite -> mapProjectAndUsernameToInvitation(invite, project)).collect(Collectors.toList());
    }
}
