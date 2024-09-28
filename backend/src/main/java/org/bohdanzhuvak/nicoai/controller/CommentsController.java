package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.security.CustomUserDetails;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentsController {
  private final CommentsService commentsService;

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteComment(@PathVariable("id") Long id,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
    commentsService.deleteComment(id, userDetails.user());
  }

}
