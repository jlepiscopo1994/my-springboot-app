package com.example.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

/**
 * This Java function handles validation exceptions by extracting error messages from the binding
 * result and returning a bad request response with the error message.
 * 
 * @param ex The `ex` parameter in the `handleValidationExceptions` method of the code snippet refers
 * to the `MethodArgumentNotValidException` that is being handled by this exception handler. This
 * exception is thrown when validation on an argument annotated with `@Valid` fails. The method
 * extracts error messages from the
 * @return The method is returning a `ResponseEntity` with a `String` body containing the error message
 * extracted from the `MethodArgumentNotValidException`. If there are validation errors in the request,
 * it will return a bad request response with the error message. If no error message is found, it will
 * return "Invalid request".
 */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");

        return ResponseEntity.badRequest().body(errorMsg);
    }

/**
 * This Java function handles IllegalArgumentException by returning a bad request response with the
 * exception message.
 * 
 * @param e The parameter `e` in the `handleNotFound` method is of type `IllegalArgumentException`. It
 * represents the exception that was thrown and caught by the `ExceptionHandler`. This parameter allows
 * you to access information about the exception, such as the error message, stack trace, or any other
 * relevant details that can help
 * @return The method is returning a ResponseEntity with a bad request status and the message from the
 * IllegalArgumentException that was caught.
 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFound(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
