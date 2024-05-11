package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;

import org.bohdanzhuvak.nicoai.dto.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.dto.AuthenticationResponse;
import org.bohdanzhuvak.nicoai.dto.RegistrationRequest;
import org.bohdanzhuvak.nicoai.service.AuthenticationService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000/")
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    @PostMapping("/signin")
    public AuthenticationResponse signin(@RequestBody AuthenticationRequest authenticationRequest) {
        
        return authenticationService.signin(authenticationRequest);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody RegistrationRequest registrationRequest) {
        authenticationService.signup(registrationRequest);
    }
}