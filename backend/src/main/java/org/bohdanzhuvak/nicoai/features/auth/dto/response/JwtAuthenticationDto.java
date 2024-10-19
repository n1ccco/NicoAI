package org.bohdanzhuvak.nicoai.features.auth.dto.response;

import lombok.Builder;
import lombok.Data;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;

@Builder
@Data
public class JwtAuthenticationDto {
  private UserDto user;
  private String token;
  private String refreshToken;
}
