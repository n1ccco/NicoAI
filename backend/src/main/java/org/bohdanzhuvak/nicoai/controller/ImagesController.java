package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;

import org.bohdanzhuvak.nicoai.dto.GenerateResponse;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.InteractionImageRequest;
import org.bohdanzhuvak.nicoai.dto.PromptRequest;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/images")
public class ImagesController {
  private final ImageService imageService;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<ImageResponse> getImages(@AuthenticationPrincipal UserDetails userDetails) {
    return imageService.getAllImages(userDetails);
  }

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public ImageResponse getImage(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
    return imageService.getImage(id, userDetails);
  }

  // @PutMapping(value = "{id}")
  // @ResponseStatus(HttpStatus.OK)
  // public void makePublic(@PathVariable("id") Long id, @RequestBody
  // ChangeImagePrivacyRequest changePrivacyRequest) {
  // imageService.changePrivacy(id, changePrivacyRequest);
  // }

  @PutMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public void upvoteImage(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails user,
      @RequestBody InteractionImageRequest interactionImageRequest) {
    imageService.changeImage(id, user, interactionImageRequest);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public GenerateResponse generateImage(@ModelAttribute PromptRequest promptRequest,
      @AuthenticationPrincipal UserDetails user) {
    return imageService.generateImage(promptRequest, user);
  }
}
