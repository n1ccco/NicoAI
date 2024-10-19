package org.bohdanzhuvak.nicoai.features.users.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.users.dto.UsernameResponse;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.bohdanzhuvak.nicoai.shared.exception.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserRepository userRepository;

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public UsernameResponse getUsername(@PathVariable("id") Long id) {
    String username = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Username of user with id: " + id + " not found"))
        .getUsername();
    return UsernameResponse.builder().username(username).build();
  }
}
