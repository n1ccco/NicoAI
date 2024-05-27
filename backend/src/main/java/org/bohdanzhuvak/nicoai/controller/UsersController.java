package org.bohdanzhuvak.nicoai.controller;

import java.util.List;

import org.bohdanzhuvak.nicoai.dto.ImageResponse;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.bohdanzhuvak.nicoai.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/users")
public class UsersController {
  private final ImageService imageService;
  private final UserRepository userRepository;

  @GetMapping(value = "{id}/images")
  @ResponseStatus(HttpStatus.OK)
  public List<ImageResponse> getImages(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails userDetails) {
    String username = userDetails.getUsername();
    Long currentUserId = userRepository.findByUsername(username).get().getId();
    if (!currentUserId.equals(id)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
    }
    return imageService.getAllUserImages(id);
  }
}
