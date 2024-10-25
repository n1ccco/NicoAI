package org.bohdanzhuvak.nicoai.users;

import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Sql(scripts = "/db/test-data/UserRepositoryTest-data.sql")
public class UserRepositoryTest {
  @Autowired
  private UserRepository userRepository;

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
