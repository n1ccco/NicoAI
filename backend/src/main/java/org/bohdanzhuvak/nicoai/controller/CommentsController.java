package org.bohdanzhuvak.nicoai.controller;

import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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
