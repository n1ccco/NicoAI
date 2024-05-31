package org.bohdanzhuvak.nicoai.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
  private Long id;
  private Long authorId;
  private String authorName;
  private String body;
  private LocalDateTime createdAt;
}
