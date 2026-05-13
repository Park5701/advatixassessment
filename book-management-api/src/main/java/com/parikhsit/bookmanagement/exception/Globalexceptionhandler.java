package com.parikhsit.bookmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * GlobalExceptionHandler — centralized error handling for all controllers.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * Every exception thrown anywhere in the controller layer is caught here
 * and converted into a consistent structured JSON error response.
 *
 * Standard error format returned:
 * {
 *   "timestamp": "2024-01-15T10:30:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Book not found with id: '99'"
 * }
 */
@RestControllerAdvice
public class Globalexceptionhandler {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /** Builds the standard error body map */
    private Map<String, Object> buildErrorBody(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().format(FORMATTER));
        body.put("status",    status.value());
        body.put("error",     status.getReasonPhrase());
        body.put("message",   message);
        return body;
    }

    // ── 404 NOT FOUND ─────────────────────────────────────────────────────────

    @ExceptionHandler(Resourcenotfoundexception.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(
            Resourcenotfoundexception ex) {
        return new ResponseEntity<>(
                buildErrorBody(HttpStatus.NOT_FOUND, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    // ── 409 CONFLICT ──────────────────────────────────────────────────────────

    @ExceptionHandler(Duplicateresourceexception.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateResource(
            Duplicateresourceexception ex) {
        return new ResponseEntity<>(
                buildErrorBody(HttpStatus.CONFLICT, ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    // ── 400 BAD REQUEST — Bean Validation failure ──────────────────────────────

    /**
     * Triggered when @Valid fails on a @RequestBody DTO.
     * Returns a field-level errors map so clients know exactly which field is wrong.
     *
     * Example response:
     * {
     *   "status": 400,
     *   "message": "Validation failed. Check 'errors' for details.",
     *   "errors": {
     *     "title": "Title is required and must not be blank",
     *     "isbn":  "ISBN must be a valid ISBN-10 or ISBN-13 format"
     *   }
     * }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fe.getField(), fe.getDefaultMessage());
        }

        Map<String, Object> body = buildErrorBody(
                HttpStatus.BAD_REQUEST,
                "Validation failed. Check 'errors' for details."
        );
        body.put("errors", fieldErrors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    // ── 400 BAD REQUEST — Type mismatch (e.g., /books/abc) ────────────────────

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        String message = String.format(
            "Invalid value '%s' for parameter '%s'. Expected type: %s",
            ex.getValue(),
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        return new ResponseEntity<>(
                buildErrorBody(HttpStatus.BAD_REQUEST, message),
                HttpStatus.BAD_REQUEST
        );
    }

    // ── 400 BAD REQUEST — Malformed JSON ──────────────────────────────────────

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                buildErrorBody(HttpStatus.BAD_REQUEST,
                        "Malformed JSON request body. Please verify your request format."),
                HttpStatus.BAD_REQUEST
        );
    }

    // ── 500 INTERNAL SERVER ERROR — catch-all ─────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                buildErrorBody(HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}