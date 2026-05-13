package com.parikhsit.bookmanagement.controller;

import com.parikhsit.bookmanagement.dto.Bookrequestdto;
import com.parikhsit.bookmanagement.dto.Bookresponsedto;
import com.parikhsit.bookmanagement.service.Bookservice;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * BookController — REST layer for all /books endpoints.
 *
 * Base URL (with context-path): http://localhost:8080/api/v1/books
 *
 * @RestController  → @Controller + @ResponseBody (auto-serialize to JSON)
 * @RequestMapping  → maps all methods under /books
 * @CrossOrigin     → allows CORS from any origin (restrict in production)
 *
 * The controller is intentionally thin:
 *   - Validates input (via @Valid)
 *   - Calls the service layer
 *   - Returns ResponseEntity with the correct HTTP status
 *   - Contains ZERO business logic
 */
@RestController
@RequestMapping("/books")
@CrossOrigin(origins = "*")
public class Bookcontroller {

    private final Bookservice bookService;

    public Bookcontroller(Bookservice bookService) {
        this.bookService = bookService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET ALL — GET /books
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns all books.
     * Response: 200 OK with JSON array (empty array [] if no books).
     */
    @GetMapping
    public ResponseEntity<List<Bookresponsedto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET BY ID — GET /books/{id}
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Returns a single book by its database ID.
     * Response: 200 OK | 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bookresponsedto> getBookById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CREATE — POST /books
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates a new book.
     * @Valid triggers Bean Validation on BookRequestDTO.
     * Response: 201 Created | 400 Bad Request | 409 Conflict
     */
    @PostMapping
    public ResponseEntity<Bookresponsedto> createBook(
            @Valid @RequestBody Bookrequestdto requestDTO) {
        Bookresponsedto created = bookService.createBook(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UPDATE — PUT /books/{id}
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Fully updates an existing book (all fields replaced).
     * Response: 200 OK | 400 Bad Request | 404 Not Found | 409 Conflict
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bookresponsedto> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody Bookrequestdto requestDTO) {
        return ResponseEntity.ok(bookService.updateBook(id, requestDTO));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE — DELETE /books/{id}
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Deletes a book permanently.
     * Response: 204 No Content | 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SEARCH — GET /books/search?author=&title=
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Advanced search — all params optional, all case-insensitive partial match.
     *
     * Examples:
     *   GET /books/search?author=martin          → by author
     *   GET /books/search?title=clean            → by title
     *   GET /books/search?author=martin&title=code → both (AND)
     *   GET /books/search                        → all books
     *
     * Response: 200 OK with matching list (empty [] if no matches)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Bookresponsedto>> searchBooks(
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title) {
        return ResponseEntity.ok(bookService.searchBooks(author, title));
    }
}