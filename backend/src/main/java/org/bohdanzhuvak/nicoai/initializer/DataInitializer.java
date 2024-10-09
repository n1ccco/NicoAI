package org.bohdanzhuvak.nicoai.initializer;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.model.User;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (!userRepository.existsByUsername("admin")) {
      userRepository.save(User.builder()
          .username("admin")
          .password(passwordEncoder.encode("password"))
          .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
          .build());
    }

  }
}