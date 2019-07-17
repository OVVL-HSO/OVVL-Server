package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ModelStorageData {

    private String modelID;

    private String username;

    private String name;

    private String projectTitle;

    private String projectID;

    private String summary;

    private String screenshot;

    private String date;
}
