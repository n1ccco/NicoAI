package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;

import org.bohdanzhuvak.nicoai.dto.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.CommentResponse;
import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.service.CommentsService;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImagesController {
  private final ImageService imageService;
  private final CommentsService commentsService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ImageResponse> getImages() {
    return imageService.getAllImages();
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
    try {
      return imageService.generateImage(promptRequest);
    } catch (IOException e) {
      return null;
    }
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
}
