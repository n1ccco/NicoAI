package org.bohdanzhuvak.nicoai.users;

import org.bohdanzhuvak.nicoai.NicoAiApplication;
import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = NicoAiApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  User user;

  @BeforeEach
  void setUp() {
    user = userRepository.findByUsername("user").orElseThrow(() -> new RuntimeException("User not found"));
  }

  @Test
  void testFindByUsername() {
    Optional<User> foundUser = userRepository.findByUsername("user");
    assertThat(foundUser.isPresent()).isTrue();
    assertThat(foundUser.get().getUsername()).isEqualTo("user");
    assertThat(foundUser.get().getPassword()).isEqualTo("password1");
  }

  @Test
  void testExistsByUsername() {
    boolean isExist = userRepository.existsByUsername("user");

    assertThat(isExist).isEqualTo(true);
  }
}
