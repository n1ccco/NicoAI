package org.bohdanzhuvak.nicoai.features.auth.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.auth.dto.request.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.features.auth.dto.request.RegistrationRequest;
import org.bohdanzhuvak.nicoai.features.auth.dto.response.JwtAuthenticationDto;
import org.bohdanzhuvak.nicoai.features.auth.dto.response.JwtRefreshResponse;
import org.bohdanzhuvak.nicoai.features.users.UserMapper;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.bohdanzhuvak.nicoai.shared.exception.AuthenticationFailedException;
import org.bohdanzhuvak.nicoai.shared.exception.TokenRefreshException;
import org.bohdanzhuvak.nicoai.shared.exception.UnauthorizedActionException;
import org.bohdanzhuvak.nicoai.shared.exception.UserAlreadyExistsException;
import org.bohdanzhuvak.nicoai.shared.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;

  public JwtAuthenticationDto login(AuthenticationRequest authenticationRequest) {
    User user = authenticateUser(authenticationRequest);
    String token = jwtTokenProvider.generateAccessToken(user);
    String refreshToken = jwtTokenProvider.generateRefreshToken(user);
    UserDto userDto = userMapper.toUserDto(user);
    return JwtAuthenticationDto.builder()
        .token(token)
        .refreshToken(refreshToken)
        .user(userDto)
        .build();
  }

  private User authenticateUser(AuthenticationRequest authenticationRequest) {
    User user = userRepository.findByUsername(authenticationRequest.getUsername())
        .orElseThrow(() -> new AuthenticationFailedException("Invalid username or password"));

    if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
      throw new AuthenticationFailedException("Invalid username or password");
    }

    return user;
  }

  public JwtAuthenticationDto register(RegistrationRequest registrationRequest) {
    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new UserAlreadyExistsException("Username is already taken");
    }

    User user = User.builder()
        .username(registrationRequest.getUsername())
        .password(passwordEncoder.encode(registrationRequest.getPassword()))
        .roles(List.of("ROLE_USER"))
        .build();

    userRepository.save(user);
    return JwtAuthenticationDto.builder()
        .token(jwtTokenProvider.generateAccessToken(user))
        .refreshToken(jwtTokenProvider.generateRefreshToken(user))
        .user(userMapper.toUserDto(user))
        .build();
  }

  public UserDto getCurrentUser(User currentUser) {
    if (isUserAuthenticated()) {
      return userMapper.toUserDto(currentUser);
    }
    throw new UnauthorizedActionException("You should be logged in");
  }

  public boolean isUserAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null &&
        authentication.isAuthenticated() &&
        !(authentication instanceof AnonymousAuthenticationToken);
  }

  public JwtRefreshResponse refreshAccessToken(String refreshToken) {
    if (jwtTokenProvider.validateToken(refreshToken)) {
      String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
      User user = userRepository.findByUsername(username)
          .orElseThrow(() -> new TokenRefreshException("User not found"));

      String newAccessToken = jwtTokenProvider.generateAccessToken(user);

      return JwtRefreshResponse.builder()
          .token(newAccessToken)
          .build();
    }
    throw new TokenRefreshException("Invalid refresh token");
  }
}
