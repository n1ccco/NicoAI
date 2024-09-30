package org.bohdanzhuvak.nicoai.service.factory;

import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.model.Comment;
import org.bohdanzhuvak.nicoai.model.Image;
import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.stereotype.Component;

@Component
public class CommentFactory {

  public Comment createComment(CommentRequest commentRequest, Image image, User author) {
    return Comment.builder()
        .author(author)
        .image(image)
        .body(commentRequest.getBody())
        .build();
  }
}