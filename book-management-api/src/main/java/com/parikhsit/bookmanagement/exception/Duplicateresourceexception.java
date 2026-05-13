package com.parikhsit.bookmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a CREATE or UPDATE would violate the ISBN uniqueness constraint.
 * Triggers HTTP 409 Conflict via GlobalExceptionHandler.
 *
 * Usage:
 *   throw new DuplicateResourceException("Book", "isbn", "978-0-13-235088-4");
 *   → message: "Book already exists with isbn: '978-0-13-235088-4'"
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class Duplicateresourceexception extends RuntimeException {

    private static final long serialVersionUID = 1L;  // ← add this line

    public Duplicateresourceexception(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists with %s: '%s'", resourceName, fieldName, fieldValue));
    }

    public Duplicateresourceexception(String message) {
        super(message);
    }
}