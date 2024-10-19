package org.bohdanzhuvak.nicoai.features.comments;

import org.bohdanzhuvak.nicoai.features.comments.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.features.comments.model.Comment;
import org.bohdanzhuvak.nicoai.features.images.model.Image;
import org.bohdanzhuvak.nicoai.features.users.model.User;
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