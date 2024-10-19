package org.bohdanzhuvak.nicoai.shared.exception;

public class TokenExpiredException extends RuntimeException {
  public TokenExpiredException(String message) {
    super(message);
  }
}