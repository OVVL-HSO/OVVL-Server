package com.tam.converter;

import com.tam.model.SettingsResource;
import com.tam.model.StoredSettingsData;

public class SettingsToStoredSettingsConverter {

    public static StoredSettingsData convert(SettingsResource settingsResource, String username) {
        return StoredSettingsData.builder()
                .username(username)
                .darktheme(settingsResource.isDarktheme())
                .build();
    }


        public static SettingsResource convertStoredSettingsToSettings(StoredSettingsData userSettings){
        return new SettingsResource()
                .darktheme(userSettings.getDarktheme());
    }

}
