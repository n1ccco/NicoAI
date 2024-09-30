package org.bohdanzhuvak.nicoai.exception;

public class UnauthorizedActionException extends RuntimeException {
  public UnauthorizedActionException(String message) {
    super(message);
  }
}