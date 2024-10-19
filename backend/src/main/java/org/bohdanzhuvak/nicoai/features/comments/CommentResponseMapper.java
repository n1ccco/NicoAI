package org.bohdanzhuvak.nicoai.features.comments;

import org.bohdanzhuvak.nicoai.features.comments.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentResponseMapper {

  public CommentResponse toCommentResponse(Comment comment) {
    return CommentResponse.builder()
        .id(comment.getId())
        .authorId(comment.getAuthor().getId())
        .authorName(comment.getAuthor().getUsername())
        .body(comment.getBody())
        .createdAt(comment.getCreatedAt())
        .build();
  }
}