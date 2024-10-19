package org.bohdanzhuvak.nicoai.features.users.repository;

import org.bohdanzhuvak.nicoai.features.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);
}
