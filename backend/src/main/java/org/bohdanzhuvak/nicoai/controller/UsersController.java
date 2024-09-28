package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.image.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.user.UsernameResponse;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.security.CustomUserDetails;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UsersController {
  private final ImageService imageService;
  private final UserRepository userRepository;

  @GetMapping(value = "{id}/images")
  @ResponseStatus(HttpStatus.OK)
  public List<ImageResponse> getImages(@PathVariable("id") Long id,
                                       @AuthenticationPrincipal @Nullable CustomUserDetails userDetails) {
    User user = (userDetails != null) ? userDetails.user() : null;
    return imageService.getAllUserImages(id, user);
  }

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public UsernameResponse getUsername(@PathVariable("id") Long id) {
    String username = userRepository.findById(id).get().getUsername();
    return UsernameResponse.builder().username(username).build();
  }
}
