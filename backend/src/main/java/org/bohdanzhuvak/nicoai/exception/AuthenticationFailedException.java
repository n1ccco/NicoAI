package org.bohdanzhuvak.nicoai.exception;

public class AuthenticationFailedException extends RuntimeException {
  public AuthenticationFailedException(String message) {
    super(message);
  }
}