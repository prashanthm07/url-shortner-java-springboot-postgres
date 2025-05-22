package com.prashanth.url_shortner.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UrlExpiredException.class)
    public ResponseEntity<CustomErrorResponse> handleUrlExpiredException(UrlExpiredException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(UrlNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleUrlNotFoundException(UrlNotFoundException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), 404);
        return ResponseEntity.status(404).body(errorResponse);
    }

    @ExceptionHandler(InvalidUrlFormatException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidUrlFormatException(InvalidUrlFormatException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(UrlNotActiveException.class)
    public ResponseEntity<CustomErrorResponse> handleUrlNotActiveException(UrlNotActiveException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse(ex.getMessage(), 400);
        return ResponseEntity.status(400).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        CustomErrorResponse errorResponse = new CustomErrorResponse("Validation failed", 400);
        return ResponseEntity.status(400).body(errorResponse);
    }
}
