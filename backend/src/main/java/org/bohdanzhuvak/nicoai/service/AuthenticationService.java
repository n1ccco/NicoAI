package org.bohdanzhuvak.nicoai.service;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.authentication.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.dto.authentication.JwtAuthenticationDto;
import org.bohdanzhuvak.nicoai.dto.authentication.JwtRefreshResponse;
import org.bohdanzhuvak.nicoai.dto.authentication.RegistrationRequest;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;
import org.bohdanzhuvak.nicoai.exception.AuthenticationFailedException;
import org.bohdanzhuvak.nicoai.exception.TokenRefreshException;
import org.bohdanzhuvak.nicoai.exception.UserAlreadyExistsException;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.jwt.JwtTokenProvider;
import org.bohdanzhuvak.nicoai.service.mapper.UserMapper;
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

  public JwtAuthenticationDto signIn(AuthenticationRequest authenticationRequest) {
    User user = authenticateUser(authenticationRequest);
    UserDto userDto = userMapper.toUserDto(user);
    String token = jwtTokenProvider.generateAccessToken(userDto);
    String refreshToken = jwtTokenProvider.generateRefreshToken(userDto);
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

  public void signUp(RegistrationRequest registrationRequest) {
    if (userRepository.existsByUsername(registrationRequest.getUsername())) {
      throw new UserAlreadyExistsException("Username is already taken");
    }

    User user = User.builder()
        .username(registrationRequest.getUsername())
        .password(passwordEncoder.encode(registrationRequest.getPassword()))
        .roles(List.of("ROLE_USER"))
        .build();

    userRepository.save(user);
  }

  public UserDto getCurrentUser(User currentUser) {
    if (isUserAuthenticated()) {
      return userMapper.toUserDto(currentUser);
    }
    return null;
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

      UserDto userDto = userMapper.toUserDto(user);
      String newAccessToken = jwtTokenProvider.generateAccessToken(userDto);

      return JwtRefreshResponse.builder()
          .token(newAccessToken)
          .build();
    }
    throw new TokenRefreshException("Invalid refresh token");
  }
}
