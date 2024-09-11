package org.bohdanzhuvak.nicoai.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
  private UserDto user;
  private String jwt;
  private String refreshToken;
}
