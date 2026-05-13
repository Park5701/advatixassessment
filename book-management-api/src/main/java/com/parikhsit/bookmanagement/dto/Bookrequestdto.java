package com.parikhsit.bookmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * BookRequestDTO — Inbound DTO for POST (create) and PUT (update) requests.
 *
 * Bean Validation annotations trigger when @Valid is used in the controller.
 * If any constraint fails, GlobalExceptionHandler returns 400 with field errors.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookrequestdto {

    @NotBlank(message = "Title is required and must not be blank")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotBlank(message = "Author name is required and must not be blank")
    @Size(min = 2, max = 150, message = "Author name must be between 2 and 150 characters")
    private String author;

    /**
     * Accepts both ISBN-10 and ISBN-13 formats, with or without hyphens.
     * Examples: 978-0-13-235088-4  |  9780132350884  |  0-13-235088-2
     */
    @NotBlank(message = "ISBN is required and must not be blank")
    @Pattern(
        regexp = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$",
        message = "ISBN must be a valid ISBN-10 or ISBN-13 format (e.g., 978-0-13-235088-4)"
    )
    private String isbn;

    @NotNull(message = "Published year is required")
    @Min(value = 1000, message = "Published year must be no earlier than 1000")
    @Max(value = 2100, message = "Published year must be no later than 2100")
    private Integer publishedYear;

    @NotNull(message = "Availability status is required (true or false)")
    private Boolean available;
}