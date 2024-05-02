package org.bohdanzhuvak.nicoai.repository;

import java.util.Optional;

import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    
}
