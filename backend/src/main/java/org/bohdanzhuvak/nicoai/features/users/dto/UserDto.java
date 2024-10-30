package org.bohdanzhuvak.nicoai.features.users.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
  private Long Id;
  private String username;
  private String role;
}
