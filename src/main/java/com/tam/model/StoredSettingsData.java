package com.tam.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StoredSettingsData {

    private String username;

    private Boolean darktheme;

}
