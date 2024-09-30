package org.bohdanzhuvak.nicoai.exception;

public class TokenRefreshException extends RuntimeException {
  public TokenRefreshException(String message) {
    super(message);
  }
}