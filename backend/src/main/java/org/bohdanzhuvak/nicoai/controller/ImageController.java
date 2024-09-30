package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.comment.CommentRequest;
import org.bohdanzhuvak.nicoai.dto.comment.CommentResponse;
import org.bohdanzhuvak.nicoai.dto.image.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.image.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.CommentService;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;
  private final CommentService commentService;

  @GetMapping
  public ResponseEntity<List<ImageResponse>> getImages(
      @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder,
      @CurrentUser @Nullable User currentUser) {

    List<ImageResponse> images = imageService.getAllImages(sortBy, sortOrder, currentUser);
    return ResponseEntity.ok(images);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ImageResponse> getImage(
      @PathVariable Long id,
      @CurrentUser @Nullable User currentUser) {

    ImageResponse imageResponse = imageService.getImage(id, currentUser);
    return ResponseEntity.ok(imageResponse);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> changeImage(
      @PathVariable Long id,
      @RequestBody InteractionImageRequest interactionImageRequest,
      @CurrentUser User currentUser) {

    imageService.changeImage(id, interactionImageRequest, currentUser);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<GenerateResponse> generateImage(
      @ModelAttribute PromptRequest promptRequest,
      @CurrentUser User currentUser) {

    GenerateResponse response = imageService.generateImage(promptRequest, currentUser);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}/comments")
  public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long id) {
    List<CommentResponse> comments = commentService.getComments(id);
    return ResponseEntity.ok(comments);
  }

  @PostMapping("/{id}/comments")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<CommentResponse> postComment(
      @RequestBody CommentRequest commentRequest,
      @PathVariable Long id,
      @CurrentUser User currentUser) {

    CommentResponse commentResponse = commentService.postComment(commentRequest, id, currentUser);
    return ResponseEntity.status(201).body(commentResponse);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> deleteImage(
      @PathVariable Long id,
      @CurrentUser User currentUser) {

    imageService.deleteImage(id, currentUser);
    return ResponseEntity.noContent().build();
  }
}