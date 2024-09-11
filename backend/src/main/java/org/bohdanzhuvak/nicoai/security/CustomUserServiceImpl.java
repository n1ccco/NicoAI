package org.bohdanzhuvak.nicoai.security;

import lombok.RequiredArgsConstructor;
import org.bohdanzhuvak.nicoai.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findByUsername(username).map(CustomUserDetails::new)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
