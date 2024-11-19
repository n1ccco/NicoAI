package org.bohdanzhuvak.nicoai.features.images.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.request.PromptRequest;
import org.bohdanzhuvak.nicoai.features.images.dto.response.*;
import org.bohdanzhuvak.nicoai.features.images.model.PromptData;
import org.bohdanzhuvak.nicoai.features.images.service.ImageService;
import org.bohdanzhuvak.nicoai.features.images.service.InteractionService;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;
  private final InteractionService interactionService;

  @GetMapping
  public ImagesResponse getImages(
      @RequestParam(name = "sortBy", defaultValue = "date") String sortBy,
      @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
      @RequestParam(name = "userId", required = false) Long userId,
      @RequestParam(name = "page", required = false) Integer page,
      @CurrentUser @Nullable User currentUser) {
    return imageService.getAllImages(sortBy, sortDirection, currentUser, userId, page);
  }

  @GetMapping("/{id}")
  public ImageResponse getImage(
      @PathVariable Long id,
      @CurrentUser @Nullable User currentUser) {
    return imageService.getImage(id, currentUser);
  }

  @GetMapping(value = "/{id}/blob")
  public ImageBlobResponse getImageBlob(
      @PathVariable Long id,
      @CurrentUser @Nullable User currentUser) {
    return imageService.getImageBlob(id, currentUser);
  }

  @GetMapping(value = "/{id}/prompt")
  public PromptData getImagePrompt(
      @PathVariable Long id,
      @CurrentUser @Nullable User currentUser) {
    return imageService.getImagePrompt(id, currentUser);
  }

  @PatchMapping("/{id}/visibility")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeImageVisibility(
      @PathVariable Long id,
      @CurrentUser User currentUser) {
    interactionService.changeImageVisibility(id, currentUser);
  }

  @PostMapping("/{id}/like")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void likeImage(
      @PathVariable Long id,
      @CurrentUser User currentUser) {
    interactionService.likeImage(id, currentUser);
  }

  @PostMapping
  public GenerateResponse generateImage(
      @RequestBody PromptRequest promptRequest,
      @CurrentUser User currentUser) {
    return imageService.generateImage(promptRequest, currentUser);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteImage(
      @PathVariable Long id,
      @CurrentUser User currentUser) {

    imageService.deleteImage(id, currentUser);
  }
}