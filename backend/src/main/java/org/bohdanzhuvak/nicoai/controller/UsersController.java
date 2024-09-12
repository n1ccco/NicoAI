package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.dto.UsernameResponse;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
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
  public List<ImageResponse> getImages(@PathVariable("id") Long id) {
    return imageService.getAllUserImages(id);
  }

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public UsernameResponse getUsername(@PathVariable("id") Long id) {
    String username = userRepository.findById(id).get().getUsername();
    return UsernameResponse.builder().username(username).build();
  }
}
