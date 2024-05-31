package org.bohdanzhuvak.nicoai.controller;

import java.util.List;

import org.bohdanzhuvak.nicoai.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/images/{imageId}/comments")
public class CommentsController {
  private final CommentsService commentsService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<CommentResponse> getComments(@PathVariable("imageId") Long id) {
    return commentsService.getComments(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void postComment(@CurrentUser UserDetails userDetails,
      @RequestBody CommentRequest commentRequest, @PathVariable("imageId") Long id) {
    commentsService.postComment(commentRequest, userDetails, id);
  }

}
