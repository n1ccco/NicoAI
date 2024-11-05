package org.bohdanzhuvak.nicoai.features.comments.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorDto {
  private Long id;
  private String username;
  private String role;
}
