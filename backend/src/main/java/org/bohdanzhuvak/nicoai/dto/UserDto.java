package org.bohdanzhuvak.nicoai.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
  private Long Id;
  private String username;
  // private List<String> roles;
}
