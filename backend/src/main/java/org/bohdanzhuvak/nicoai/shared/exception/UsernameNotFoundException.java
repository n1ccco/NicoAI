package org.bohdanzhuvak.nicoai.shared.exception;

public class UsernameNotFoundException extends RuntimeException {
  public UsernameNotFoundException(String message) {
    super(message);
  }
}
