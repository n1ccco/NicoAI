package org.bohdanzhuvak.nicoai.shared.security;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.features.users.repository.UserRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  @Cacheable(value = "user_details", key = "#username")
  public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
