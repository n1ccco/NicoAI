package org.bohdanzhuvak.nicoai.exception;

public class CommentNotFoundException extends RuntimeException {
  public CommentNotFoundException(String message) {
    super(message);
  }
}