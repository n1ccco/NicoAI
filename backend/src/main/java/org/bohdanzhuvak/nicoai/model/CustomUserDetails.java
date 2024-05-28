package org.bohdanzhuvak.nicoai.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static java.util.stream.Collectors.toList;

public class CustomUserDetails extends User implements UserDetails {

  public CustomUserDetails(User user) {
    super(user.getId(), user.getImages(), user.getUsername(), user.getPassword(), user.getLikes(), user.getRoles());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles().stream().map(SimpleGrantedAuthority::new).collect(toList());
  }

  @Override
  public String getUsername() {
    return getUsername();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
