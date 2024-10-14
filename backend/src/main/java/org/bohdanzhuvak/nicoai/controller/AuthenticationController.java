package org.bohdanzhuvak.nicoai.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.authentication.*;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.security.jwt.JwtProperties;
import org.bohdanzhuvak.nicoai.service.AuthenticationService;
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