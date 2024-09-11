package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.*;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CustomUserDetails;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
  public List<ImageResponse> getImages(
      @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder) {
    return imageService.getAllImages(sortBy, sortOrder);
  }

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public ImageResponse getImage(@PathVariable("id") Long id) {
    return imageService.getImage(id);
  }

  @PatchMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public void changeImage(@PathVariable("id") Long id, @RequestBody InteractionImageRequest interactionImageRequest) {
    imageService.changeImage(id, interactionImageRequest);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public GenerateResponse generateImage(@ModelAttribute PromptRequest promptRequest) {
    User author = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal()).user();
    return imageService.generateImage(promptRequest, author);
  }

  @GetMapping("{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  public List<CommentResponse> getComments(@PathVariable("id") Long id) {
    return commentsService.getComments(id);
  }

  @PostMapping(value = "{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  public CommentResponse postComment(@RequestBody CommentRequest commentRequest, @PathVariable("id") Long id) {
    return commentsService.postComment(commentRequest, id);
  }

  @DeleteMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteImage(@PathVariable("id") Long id) {
    imageService.deleteImage(id);
  }
}
