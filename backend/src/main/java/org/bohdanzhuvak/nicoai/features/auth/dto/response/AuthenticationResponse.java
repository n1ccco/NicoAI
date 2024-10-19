package org.bohdanzhuvak.nicoai.features.auth.dto.response;

import lombok.Builder;
import lombok.Data;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;

@Data
@Builder
public class AuthenticationResponse {
  private UserDto user;
  private String jwt;
}
