package org.bohdanzhuvak.nicoai.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.bohdanzhuvak.nicoai.NicoAiApplication;
import org.bohdanzhuvak.nicoai.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = NicoAiApplication.class)
@ActiveProfiles("test")
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(1L);
    user.setUsername("john_doe");
    user.setPassword("password123");
    userRepository.save(user);
  }

  @Test
  void testFindByUsername() {
    User foundUser = userRepository.findByUsername("john_doe");

    assertThat(foundUser.getUsername()).isEqualTo("john_doe");
    assertThat(foundUser.getPassword()).isEqualTo("password123");
  }

  @Test
  void testExistsByUsername() {
    boolean isExist = userRepository.existsByUsername("john_doe");

    assertThat(isExist).isEqualTo(true);
  }
}
