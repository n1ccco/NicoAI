package org.bohdanzhuvak.nicoai.features.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtRefreshResponse {
  private String token;
}
