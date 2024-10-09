package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @GetMapping("/api/images/{imageId}/comments")
  public List<CommentResponse> getComments(@PathVariable Long imageId) {
    return commentService.getComments(imageId);
  }

  @PostMapping("/api/images/{imageId}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse postComment(
      @PathVariable Long imageId,
      @RequestBody CommentRequest commentRequest,
      @CurrentUser User currentUser) {
    return commentService.postComment(commentRequest, imageId, currentUser);
  }

  @DeleteMapping("/api/comments/{commentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(
      @PathVariable Long commentId,
      @CurrentUser User user) {
    commentService.deleteComment(commentId, user);
  }
}