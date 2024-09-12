package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {
  private final CommentsService commentsService;

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteComment(@CurrentUser UserDetails userDetails, @PathVariable("id") Long id) {
    commentsService.deleteComment(userDetails, id);
  }

}
