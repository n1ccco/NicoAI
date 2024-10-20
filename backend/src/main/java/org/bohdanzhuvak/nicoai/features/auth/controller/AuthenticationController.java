package org.bohdanzhuvak.nicoai.features.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.auth.dto.request.AuthenticationRequest;
import org.bohdanzhuvak.nicoai.features.auth.dto.request.RegistrationRequest;
import org.bohdanzhuvak.nicoai.features.auth.dto.response.AuthenticationResponse;
import org.bohdanzhuvak.nicoai.features.auth.dto.response.JwtAuthenticationDto;
import org.bohdanzhuvak.nicoai.features.auth.dto.response.JwtRefreshResponse;
import org.bohdanzhuvak.nicoai.features.auth.service.AuthenticationService;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.security.CurrentUser;
import org.bohdanzhuvak.nicoai.shared.security.jwt.JwtProperties;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final JwtProperties jwtProperties;
  private final AuthenticationService authenticationService;

  @PostMapping("/signin")
  public AuthenticationResponse signin(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws AuthenticationException {
    JwtAuthenticationDto jwtAuthenticationDto = authenticationService.signIn(authenticationRequest);

    Cookie refreshTokenCookie = new Cookie("refreshToken", jwtAuthenticationDto.getRefreshToken());
    refreshTokenCookie.setHttpOnly(false);
    refreshTokenCookie.setSecure(false);
    refreshTokenCookie.setPath("/");
    refreshTokenCookie.setMaxAge((int) jwtProperties.getValidityRefresh().toMillis());
    response.addCookie(refreshTokenCookie);

    return AuthenticationResponse.builder()
        .user(jwtAuthenticationDto.getUser())
        .jwt(jwtAuthenticationDto.getToken())
        .build();
  }

  @PostMapping("/signup")
  public void signup(@RequestBody RegistrationRequest registrationRequest) {
    authenticationService.signUp(registrationRequest);
  }

  @PostMapping("/refresh")
  public JwtRefreshResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    Cookie[] cookies = request.getCookies();
    String refreshToken = null;
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refreshToken".equals(cookie.getName())) {
          refreshToken = cookie.getValue();
          break;
        }
      }
    }
    if (refreshToken == null) {
      throw new AuthenticationException("Refresh token not found in cookies");
    }

    return authenticationService.refreshAccessToken(refreshToken);
  }

  @GetMapping("/me")
  public UserDto getCurrentUser(@CurrentUser User user) {
    return authenticationService.getCurrentUser(user);
  }
}