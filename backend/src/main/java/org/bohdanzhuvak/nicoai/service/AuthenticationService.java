package org.bohdanzhuvak.nicoai.service;

import java.util.Arrays;

import org.bohdanzhuvak.nicoai.dto.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.dto.AuthenticationResponse;
import org.bohdanzhuvak.nicoai.dto.RegistrationRequest;
import org.bohdanzhuvak.nicoai.dto.UserDto;
import org.bohdanzhuvak.nicoai.model.CustomUserDetails;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
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

  public AuthenticationResponse signin(AuthenticationRequest authenticationRequest) {
    try {
      String username = authenticationRequest.getUsername();
      String password = authenticationRequest.getPassword();
      var authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));
      String token = jwtTokenProvider.createToken(authentication);
      User user = ((CustomUserDetails) jwtTokenProvider.getAuthentication(token).getPrincipal()).getUser();
      return AuthenticationResponse.builder().jwt(token)
          .user(UserDto.builder().Id(user.getId()).username(user.getUsername()).roles(user.getRoles()).build()).build();
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid username/password supplied");
    }
  }

  public void signup(RegistrationRequest registrationRequest) {
    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    userRepository.save(User.builder().username(registrationRequest.getUsername())
        .password(passwordEncoder.encode(registrationRequest.getPassword()))
        .roles(Arrays.asList("ROLE_USER")).build());
  }

  public UserDto getCurrentUser() {
    if (isUserAuthenticated()) {
      User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
          .getPrincipal()).getUser();

      return UserDto.builder().Id(user.getId()).username(user.getUsername()).roles(user.getRoles()).build();
    }
    return null;
  }

  public boolean isUserAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated() &&
        !(authentication.getPrincipal() instanceof String);
  }
}
