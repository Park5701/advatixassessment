package com.parikhsit.bookmanagement.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Book — JPA Entity mapped to the `books` table in MySQL.
 *
 * Lombok annotations:
 *   @Data               → generates getters, setters, toString, equals, hashCode
 *   @NoArgsConstructor  → required by JPA specification
 *   @AllArgsConstructor → used by the @Builder
 *   @Builder            → fluent builder: Book.builder().title("...").build()
 *
 * JPA annotations:
 *   @Entity             → marks this class as a database entity
 *   @Table              → maps to "books" table; unique constraint on isbn column
 *   @Id                 → primary key
 *   @GeneratedValue     → auto-increment by MySQL (IDENTITY strategy)
 *   @Column             → fine-tunes column name, nullability, length
 */
@Entity
@Table(
    name = "books",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_books_isbn", columnNames = "isbn")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", nullable = false, length = 150)
    private String author;

    /** ISBN-10 or ISBN-13 — must be unique across all books */
    @Column(name = "isbn", nullable = false, unique = true, length = 17)
    private String isbn;

    @Column(name = "published_year", nullable = false)
    private Integer publishedYear;

    /** true = available for borrowing, false = currently unavailable */
    @Column(name = "available", nullable = false)
    @Builder.Default
    private Boolean available = true;
}