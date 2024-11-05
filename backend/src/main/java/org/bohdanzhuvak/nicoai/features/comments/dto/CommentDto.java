package org.bohdanzhuvak.nicoai.features.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
  private Long id;
  private String body;
  private AuthorDto author;
  private Instant createdAt;
}
