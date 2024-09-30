package org.bohdanzhuvak.nicoai.exception;

public class UsernameNotFoundException extends RuntimeException {
  public UsernameNotFoundException(String message) {
    super(message);
  }
}
