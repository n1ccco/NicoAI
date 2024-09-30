package org.bohdanzhuvak.nicoai.exception;

public class TokenExpiredException extends RuntimeException {
  public TokenExpiredException(String message) {
    super(message);
  }
}