package org.bohdanzhuvak.nicoai.exception;

public class ImageNotFoundException extends RuntimeException {
  public ImageNotFoundException(String message) {
    super(message);
  }
}