package com.tam.api.controller;


import com.tam.api.SettingsApi;
import com.tam.converter.SettingsToStoredSettingsConverter;
import com.tam.model.*;
import com.tam.repositories.*;
import com.tam.security.Jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@EnableAutoConfiguration
@RestController
public class SettingsApiController implements SettingsApi {

    private JwtProvider jwtProvider;
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsApiController(JwtProvider jwtProvider,
                                 SettingsRepository settingsRepository) {
        this.jwtProvider = jwtProvider;
        this.settingsRepository = settingsRepository;
    }

    @Override
    public ResponseEntity<SettingsResource> getUserSettings(String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        StoredSettingsData userSettings = settingsRepository.findSettingsByUsername(username).orElse(null);

        if(userSettings != null){
            SettingsResource settingsResource = SettingsToStoredSettingsConverter.convertStoredSettingsToSettings(userSettings);
            return new ResponseEntity(settingsResource, HttpStatus.OK);
        }

        return new ResponseEntity(userSettings, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<Void> saveUserSettings(String authorization, SettingsResource settingsResource) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);

        System.out.println("Saving Settings for user :" + username);
        StoredSettingsData storedSettingsData = SettingsToStoredSettingsConverter.convert(settingsResource, username);

        // delete existing settings
        settingsRepository.deleteByUsername(username);
        // save new user settings
        settingsRepository.save(storedSettingsData);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
