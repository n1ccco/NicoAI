package org.bohdanzhuvak.nicoai.repository;

import org.bohdanzhuvak.nicoai.NicoAiApplication;
import org.bohdanzhuvak.nicoai.model.User;
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

  @BeforeEach
  void setUp() {
    User user = new User();
    user.setId(1L);
    user.setUsername("john_doe");
    user.setPassword("password123");
    userRepository.save(user);
  }

  @Test
  void testFindByUsername() {
    Optional<User> foundUser = userRepository.findByUsername("john_doe");

    assertThat(foundUser.get().getUsername()).isEqualTo("john_doe");
    assertThat(foundUser.get().getPassword()).isEqualTo("password123");
  }

  @Test
  void testExistsByUsername() {
    boolean isExist = userRepository.existsByUsername("john_doe");

    assertThat(isExist).isEqualTo(true);
  }
}
