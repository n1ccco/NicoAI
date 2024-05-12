package org.bohdanzhuvak.nicoai.service;

import java.util.Arrays;

import org.bohdanzhuvak.nicoai.dto.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.dto.AuthenticationResponse;
import org.bohdanzhuvak.nicoai.dto.RegistrationRequest;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse signin(AuthenticationRequest authenticationRequest){
        try {
            String username = authenticationRequest.getUsername();
            Long userId = userRepository.findByUsername(username).get().getId();
            String password = authenticationRequest.getPassword();
            var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createToken(authentication);
            return AuthenticationResponse
                .builder()
                .username(username)
                .userId(userId)
                .token(token)
                .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }


    public void signup(RegistrationRequest registrationRequest){
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        
        userRepository.save(User.builder()
            .username(registrationRequest.getUsername())
            .password(passwordEncoder.encode(registrationRequest.getPassword()))
            .roles(Arrays.asList("ROLE_USER"))
            .build()
        );
    }

}
