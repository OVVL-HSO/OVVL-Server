package com.tam.api.controller;

import com.tam.api.AuthApi;
import com.tam.model.*;
import com.tam.repositories.UserRepository;
import com.tam.response.JwtResponse;
import com.tam.security.Jwt.JwtProvider;
import com.tam.validator.PasswordValidator;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@EnableAutoConfiguration
@RestController
public class AuthApiController implements AuthApi {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder encoder;
    private JwtProvider jwtProvider;

    @Autowired
    public AuthApiController(AuthenticationManager authManager,
                             UserRepository userRepo,
                             PasswordEncoder pwEncoder,
                             JwtProvider jwtProvider) {
        this.authenticationManager = authManager;
        this.userRepository = userRepo;
        this.encoder = pwEncoder;
        this.jwtProvider = jwtProvider;
    }

    @CrossOrigin
    @Override
    public ResponseEntity<Object> signIn(@ApiParam(value = "User Credentials"  )  @Valid @RequestBody UserResource loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }

    @CrossOrigin
    @Override
    public  ResponseEntity<Object> signUp(@ApiParam(value = "User Credentials"  )  @Valid @RequestBody UserResource signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Username is already taken."),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("Email is already in use."),
                    HttpStatus.BAD_REQUEST);
        }

        if (!PasswordValidator.isValid(signUpRequest.getPassword())) {
            return new ResponseEntity<>(new ResponseMessage("Please try a different password."),
                    HttpStatus.BAD_REQUEST);
        }
        // Creating user's account
        UserResource user = new UserResource()
                .username(signUpRequest.getUsername())
                .email(signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return new ResponseEntity<>(new ResponseMessage("User registered successfully!"), HttpStatus.OK);
    }
}

