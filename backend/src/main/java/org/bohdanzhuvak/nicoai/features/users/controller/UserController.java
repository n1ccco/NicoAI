package org.bohdanzhuvak.nicoai.features.users.controller;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.users.dto.UserDto;
import org.bohdanzhuvak.nicoai.features.users.dto.UsernameResponse;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.bohdanzhuvak.nicoai.features.users.service.UserService;
import org.bohdanzhuvak.nicoai.shared.exception.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
  private final UserRepository userRepository;
  private final UserService userService;

  @GetMapping(value = "{id}")
  @ResponseStatus(HttpStatus.OK)
  public UsernameResponse getUsername(@PathVariable("id") Long id) {
    String username = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("Username of user with id: " + id + " not found"))
        .getUsername();
    return UsernameResponse.builder().username(username).build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserDto> getUsers() {
    return userService.getUsers();
  }

  @DeleteMapping(value = "{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }
}
