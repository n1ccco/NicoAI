package org.bohdanzhuvak.nicoai.exception.handler;

import org.bohdanzhuvak.nicoai.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(AuthenticationFailedException.class)
  public ResponseEntity<String> handleAuthenticationFailedException(AuthenticationFailedException ex) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication Failed: " + ex.getMessage());
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  @ExceptionHandler(TokenRefreshException.class)
  public ResponseEntity<String> handleTokenRefreshException(TokenRefreshException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }

  @ExceptionHandler({ImageNotFoundException.class, CommentNotFoundException.class})
  public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image Not Found: " + ex.getMessage());
  }

  @ExceptionHandler(FileStorageException.class)
  public ResponseEntity<String> handleFileStorageException(FileStorageException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File Storage Error: " + ex.getMessage());
  }

  @ExceptionHandler(ImageGenerationException.class)
  public ResponseEntity<String> handleImageGenerationException(ImageGenerationException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Image generation failed: " + ex.getMessage());
  }

  @ExceptionHandler(UnauthorizedActionException.class)
  public ResponseEntity<String> handleUnauthorizedActionException(UnauthorizedActionException ex) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGenericException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
  }

}