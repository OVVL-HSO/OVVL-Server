package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Project {
  private String projectID;
  private String owner;
  private List<String> collaborators;
  private List<String> invites;
  private List<String> models;
  private String title;
  private String description;
  private String creationDate;
}
