package org.bohdanzhuvak.nicoai.shared.exception;

public class UnauthorizedActionException extends RuntimeException {
  public UnauthorizedActionException(String message) {
    super(message);
  }
}