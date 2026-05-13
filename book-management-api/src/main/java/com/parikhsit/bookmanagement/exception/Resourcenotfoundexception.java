package com.parikhsit.bookmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a requested Book (or any resource) cannot be found.
 * Triggers HTTP 404 Not Found via GlobalExceptionHandler.
 *
 * Usage:
 *   throw new ResourceNotFoundException("Book", "id", 42L);
 *   → message: "Book not found with id: '42'"
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class Resourcenotfoundexception extends RuntimeException {

    private static final long serialVersionUID = 1L;  // ← add this line

    public Resourcenotfoundexception(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public Resourcenotfoundexception(String message) {
        super(message);
    }
}