package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
  private final CommentService commentService;

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteComment(@PathVariable("id") Long id,
                            @CurrentUser User user) {
    commentService.deleteComment(id, user);
  }

}
