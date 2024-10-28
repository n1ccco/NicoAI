package org.bohdanzhuvak.nicoai.features.comments;

import org.bohdanzhuvak.nicoai.features.comments.dto.AuthorDto;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentDto;
import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentResponseMapper {

  public CommentDto toCommentResponse(Comment comment) {
    User author = comment.getAuthor();
    return CommentDto.builder()
        .id(comment.getId())
        .author(AuthorDto.builder()
            .id(author.getId())
            .username(author.getUsername())
            .role(author.getRoles().getFirst()).build())
        .body(comment.getBody())
        .createdAt(comment.getCreatedAt())
        .build();
  }
}