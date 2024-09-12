package org.bohdanzhuvak.nicoai.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  private Long id;
  private Long authorId;
  private String authorName;
  private String body;
  private Instant createdAt;
}
