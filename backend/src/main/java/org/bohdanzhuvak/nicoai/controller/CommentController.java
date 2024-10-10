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