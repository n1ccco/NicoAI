package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.dto.image.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.image.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CustomUserDetails;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImagesController {
  private final ImageService imageService;
  private final CommentsService commentsService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ImageResponse> getImages(@RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
                                       @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder,
                                       @AuthenticationPrincipal @Nullable CustomUserDetails userDetails) {
    if (userDetails == null) {
      return imageService.getAllImages(sortBy, sortOrder);
    } else {
      return imageService.getAllImages_Authenticated(sortBy, sortOrder, userDetails.user());
    }
  }

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public ImageResponse getImage(@PathVariable("id") Long id,
                                @AuthenticationPrincipal @Nullable CustomUserDetails userDetails) {
    User user = (userDetails != null) ? userDetails.user() : null;
    return imageService.getImage(id, user);
  }

  @PatchMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public void changeImage(@PathVariable("id") Long id,
                          @RequestBody InteractionImageRequest interactionImageRequest,
                          @AuthenticationPrincipal CustomUserDetails userDetails) {
    imageService.changeImage(id, interactionImageRequest, userDetails.user());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public GenerateResponse generateImage(@ModelAttribute PromptRequest promptRequest,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
    return imageService.generateImage(promptRequest, userDetails.user());
  }

  @GetMapping("{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  public List<CommentResponse> getComments(@PathVariable("id") Long id) {
    return commentsService.getComments(id);
  }

  @PostMapping(value = "{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse postComment(@RequestBody CommentRequest commentRequest,
                                     @PathVariable("id") Long id,
                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
    return commentsService.postComment(commentRequest, id, userDetails.user());
  }

  @DeleteMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteImage(@PathVariable("id") Long id) {
    imageService.deleteImage(id);
  }
}
