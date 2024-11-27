package org.bohdanzhuvak.nicoai.features.images.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.images.dto.response.ImagesResponse;
import org.bohdanzhuvak.nicoai.features.images.service.ImageSearchService;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.shared.security.CurrentUser;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageSearchController {

  private final ImageSearchService imageSearchService;

  @GetMapping("/search")
  @ResponseStatus(HttpStatus.OK)
  public ImagesResponse searchImages(
      @RequestParam(required = false) String keyword,
      @RequestParam(name = "page", required = false) Integer page,
      @CurrentUser @Nullable User currentUser) {

    return imageSearchService.searchImages(keyword, currentUser, page, 6);
  }
}
