package com.parikhsit.bookmanagement.service;

import com.parikhsit.bookmanagement.dto.Bookrequestdto;
import com.parikhsit.bookmanagement.dto.Bookresponsedto;

import java.util.List;

/**
 * BookService — service layer contract (interface).
 *
 * The controller depends on this interface, not the concrete class.
 * Spring injects BookServiceImpl at runtime.
 *
 * Benefits of using an interface here:
 *   - Loose coupling between controller and business logic
 *   - Easy to mock in unit tests
 *   - Spring AOP (@Transactional) works through the proxy
 */
public interface Bookservice {

    /** Get all books (returns empty list if none exist) */
    List<Bookresponsedto> getAllBooks();

    /** Get a single book by ID — throws ResourceNotFoundException if not found */
    Bookresponsedto getBookById(Long id);

    /** Create a new book — throws DuplicateResourceException if ISBN already exists */
    Bookresponsedto createBook(Bookrequestdto requestDTO);

    /**
     * Full update of an existing book.
     * Throws ResourceNotFoundException if book not found.
     * Throws DuplicateResourceException if new ISBN conflicts with another book.
     */
    Bookresponsedto updateBook(Long id, Bookrequestdto requestDTO);

    /** Delete a book by ID — throws ResourceNotFoundException if not found */
    void deleteBook(Long id);

    /**
     * Search books flexibly:
     *   both author + title → AND search
     *   author only         → search by author
     *   title only          → search by title
     *   neither             → return all books
     * All searches are case-insensitive and partial-match.
     */
    List<Bookresponsedto> searchBooks(String author, String title);
}