package org.bohdanzhuvak.nicoai.dto.authentication;

import lombok.Builder;
import lombok.Data;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;

@Builder
@Data
public class JwtAuthenticationDto {
  private UserDto user;
  private String token;
  private String refreshToken;
}
