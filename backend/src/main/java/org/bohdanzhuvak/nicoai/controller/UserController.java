package org.bohdanzhuvak.nicoai.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.dto.user.UsernameResponse;
import org.bohdanzhuvak.nicoai.exception.UsernameNotFoundException;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
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
