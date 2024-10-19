package org.bohdanzhuvak.nicoai.features.users.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserDto {
  private Long Id;
  private String username;
  private List<String> roles;
}
