package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.image.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.image.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.image.PromptRequest;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.security.CurrentUser;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  @GetMapping
  public List<ImageResponse> getImages(
      @RequestParam(name = "sortBy", required = false, defaultValue = "date") String sortBy,
      @RequestParam(name = "order", required = false, defaultValue = "asc") String sortOrder,
      @CurrentUser @Nullable User currentUser) {

    return imageService.getAllImages(sortBy, sortOrder, currentUser);
  }

  @GetMapping("/{id}")
  public ImageResponse getImage(
      @PathVariable Long id,
      @CurrentUser @Nullable User currentUser) {

    return imageService.getImage(id, currentUser);
  }

  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void changeImage(
      @PathVariable Long id,
      @RequestBody InteractionImageRequest interactionImageRequest,
      @CurrentUser User currentUser) {

    imageService.changeImage(id, interactionImageRequest, currentUser);
  }

  @PostMapping
  public GenerateResponse generateImage(
      @ModelAttribute PromptRequest promptRequest,
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