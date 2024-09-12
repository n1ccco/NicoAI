package org.bohdanzhuvak.nicoai.dto.user;

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
