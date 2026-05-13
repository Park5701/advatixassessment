package com.parikhsit.bookmanagement.dto;

import lombok.*;

/**
 * BookResponseDTO — Outbound DTO returned in all successful API responses.
 *
 * Keeps the API surface explicit — only these fields are exposed to clients.
 * Internal DB details (e.g., audit columns, lazy-loaded relations) stay hidden.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bookresponsedto {

    private Long    id;
    private String  title;
    private String  author;
    private String  isbn;
    private Integer publishedYear;
    private Boolean available;
}