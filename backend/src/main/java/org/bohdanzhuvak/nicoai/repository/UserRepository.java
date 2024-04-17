package org.bohdanzhuvak.nicoai.repository;

import org.bohdanzhuvak.nicoai.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
