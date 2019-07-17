package com.tam.api.controller;

import com.tam.api.ProfileApi;
import com.tam.model.*;
import com.tam.repositories.ProjectRepository;
import com.tam.repositories.StorageDataRepository;
import com.tam.repositories.UserRepository;
import com.tam.security.Jwt.JwtProvider;
import com.tam.utils.MapUtil;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@EnableAutoConfiguration
@RestController
public class ProfileApiController implements ProfileApi {

    private JwtProvider jwtProvider;
    private UserRepository userRepository;

    @Autowired
    public ProfileApiController(JwtProvider jwtProvider,
                                UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    @CrossOrigin
    @Override
    public ResponseEntity<ProfileDataResource> getProfileInfo(String authorization) {
        return new ResponseEntity<>(createProfileData(authorization), HttpStatus.OK);
    }

    private ProfileDataResource createProfileData(String authorization) {
        String username = jwtProvider.getUsernameFromAuthHeader(authorization);
        UserResource user = userRepository.findByUsername(username).orElse(null);
        return new ProfileDataResource()
                .mail(user.getEmail())
                .username(username);
    }

    @CrossOrigin
    @Override
    public ResponseEntity<UserExistsResource> checkForUsername(String username) {
        if (username == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        UserResource user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new UserExistsResource().exists(false), HttpStatus.OK);
        }
        return new ResponseEntity<>(new UserExistsResource().exists(true), HttpStatus.OK);
    }
}
