package org.bohdanzhuvak.nicoai.dto;

import lombok.Data;

@Data
public class JwtAuthenticationDto {
  private String token;
  private String refreshToken;
}
