package org.bohdanzhuvak.nicoai.features.comments.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.features.comments.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.features.comments.service.CommentService;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @GetMapping
  public List<CommentResponse> getComments(@RequestParam(name = "imageId", required = false) Long imageId) {
    return commentService.getComments(imageId);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CommentResponse postComment(
      @RequestBody CommentRequest commentRequest,
      @CurrentUser User currentUser) {
    return commentService.postComment(commentRequest, currentUser);
  }

  @DeleteMapping("{commentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(
      @PathVariable Long commentId,
      @CurrentUser User user) {
    commentService.deleteComment(commentId, user);
  }
}