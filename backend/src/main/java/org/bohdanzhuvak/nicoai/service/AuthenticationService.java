package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.*;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationResponse signin(AuthenticationRequest authenticationRequest) throws AuthenticationException {
    User user = findByCredentials(authenticationRequest);
    UserDto userDto = UserDto
        .builder()
        .Id(user.getId())
        .username(user.getUsername())
        .roles(user.getRoles())
        .build();
    String token = jwtTokenProvider.generateJwtToken(userDto);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDto);
    return AuthenticationResponse
        .builder()
        .jwt(token)
        .refreshToken(refreshToken)
        .user(userDto)
        .build();
  }

  private User findByCredentials(AuthenticationRequest authenticationRequest) throws AuthenticationException {
    Optional<User> optionalUser = userRepository.findByUsername(authenticationRequest.getUsername());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
        return user;
      }
    }
    throw new AuthenticationException("Email or password is not correct");
  }

  public void signup(RegistrationRequest registrationRequest) {
    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new IllegalArgumentException("Username is already taken");
    }

    userRepository.save(User.builder().username(registrationRequest.getUsername())
        .password(passwordEncoder.encode(registrationRequest.getPassword()))
        .roles(List.of("ROLE_USER")).build());
  }

  public UserDto getCurrentUser(String token) {
    if (isUserAuthenticated()) {
      Optional<User> optionalUser = userRepository.findByUsername(jwtTokenProvider.getUsernameFromToken(token));
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        return UserDto.builder()
            .Id(user.getId())
            .username(user.getUsername())
            .roles(user.getRoles())
            .build();
      }
    }
    return null;
  }

  public boolean isUserAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null && authentication.isAuthenticated() &&
        !(authentication.getPrincipal() instanceof String);
  }

  public JwtAuthenticationDto refreshToken(JwtRefreshDto jwtRefreshDto) throws AuthenticationException {
    String refreshToken = jwtRefreshDto.getToken();
    if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
      User user = userRepository.findByUsername(jwtTokenProvider.getUsernameFromToken(refreshToken)).orElseThrow(() -> new AuthenticationException("Failed to refresh token"));
      return jwtTokenProvider.refreshBaseToken(UserDto.builder().Id(user
              .getId())
          .roles(user.getRoles())
          .username(user.getUsername())
          .build(), refreshToken);
    }
    throw new AuthenticationException("Invalid refresh token");
  }
}
