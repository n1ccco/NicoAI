package org.bohdanzhuvak.nicoai.shared.exception;

public class TokenRefreshException extends RuntimeException {
  public TokenRefreshException(String message) {
    super(message);
  }
}