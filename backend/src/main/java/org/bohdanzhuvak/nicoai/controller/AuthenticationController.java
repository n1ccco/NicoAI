package org.bohdanzhuvak.nicoai.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.*;
import org.bohdanzhuvak.nicoai.service.AuthenticationService;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

import static org.bohdanzhuvak.nicoai.utils.TokenUtils.resolveToken;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping("/signin")
  public AuthenticationResponse signin(@RequestBody AuthenticationRequest authenticationRequest) throws AuthenticationException {
    return authenticationService.signin(authenticationRequest);
  }

  @PostMapping("/signup")
  public void signup(@RequestBody RegistrationRequest registrationRequest) {
    authenticationService.signup(registrationRequest);
  }

  @PostMapping("/refresh")
  public JwtAuthenticationDto refreshToken(@RequestBody JwtRefreshDto jwtRefreshDto) throws AuthenticationException {
    return authenticationService.refreshToken(jwtRefreshDto);
  }

  @GetMapping("/me")
  public UserDto getCurrentUser(HttpServletRequest request) {
    String token = resolveToken(request);
    return authenticationService.getCurrentUser(token);
  }
}