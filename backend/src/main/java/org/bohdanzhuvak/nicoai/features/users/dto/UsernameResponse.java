package org.bohdanzhuvak.nicoai.features.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class UsernameResponse {
  private String username;

}
