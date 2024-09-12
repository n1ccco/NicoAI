package org.bohdanzhuvak.nicoai.dto.authentication;

import lombok.Builder;
import lombok.Data;
import org.bohdanzhuvak.nicoai.dto.user.UserDto;

@Data
@Builder
public class AuthenticationResponse {
  private UserDto user;
  private String jwt;
}
