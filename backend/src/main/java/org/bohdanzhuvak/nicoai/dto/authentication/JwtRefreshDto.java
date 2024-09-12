package org.bohdanzhuvak.nicoai.dto.authentication;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtRefreshDto {
  private String token;
  private String refreshToken;
}
