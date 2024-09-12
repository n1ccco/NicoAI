package org.bohdanzhuvak.nicoai.dto.authentication;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtRefreshResponse {
  private String token;
}
