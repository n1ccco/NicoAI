package org.bohdanzhuvak.nicoai.service.mapper;

import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.model.Comment;
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