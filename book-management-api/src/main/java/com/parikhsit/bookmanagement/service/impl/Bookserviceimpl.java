package com.parikhsit.bookmanagement.service.impl;

import com.parikhsit.bookmanagement.dto.Bookrequestdto;
import com.parikhsit.bookmanagement.dto.Bookresponsedto;
import com.parikhsit.bookmanagement.model.Book;
import com.parikhsit.bookmanagement.exception.Duplicateresourceexception;
import com.parikhsit.bookmanagement.exception.Resourcenotfoundexception;
import com.parikhsit.bookmanagement.repository.Bookrepository;
import com.parikhsit.bookmanagement.service.Bookservice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BookServiceImpl — concrete implementation of BookService.
 *
 * Key design decisions:
 *   @Service               → registers this bean in Spring context
 *   Constructor injection  → deps are final, explicit, testable without Spring
 *   @Transactional(readOnly=true) at class level → all methods read-only by default
 *   @Transactional (no readOnly) on write methods → overrides class-level for commits
 *
 * toEntity() and toResponseDTO() are private mapper helpers.
 * In larger projects, replace these with MapStruct for compile-time safety.
 */
@Service
@Transactional(readOnly = true)
public class Bookserviceimpl implements Bookservice {

    private final Bookrepository bookRepository;

    /**
     * Constructor injection — Spring injects BookRepository automatically.
     * No @Autowired needed (single constructor rule, Spring 4.3+).
     */
    public Bookserviceimpl(Bookrepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // =========================================================================
    // READ
    // =========================================================================

    @Override
    public List<Bookresponsedto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Bookresponsedto getBookById(Long id) {
        Book book = findOrThrow(id);
        return toResponseDTO(book);
    }

    // =========================================================================
    // WRITE
    // =========================================================================

    @Override
    @Transactional
    public Bookresponsedto createBook(Bookrequestdto dto) {
        // Guard: check ISBN uniqueness before hitting the DB unique constraint
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new Duplicateresourceexception("Book", "isbn", dto.getIsbn());
        }
        Book saved = bookRepository.save(toEntity(dto));
        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public Bookresponsedto updateBook(Long id, Bookrequestdto dto) {
        Book existing = findOrThrow(id);

        // Allow the book to keep its own ISBN, but block conflict with OTHER books
        if (bookRepository.existsByIsbnAndIdNot(dto.getIsbn(), id)) {
            throw new Duplicateresourceexception("Book", "isbn", dto.getIsbn());
        }

        existing.setTitle(dto.getTitle());
        existing.setAuthor(dto.getAuthor());
        existing.setIsbn(dto.getIsbn());
        existing.setPublishedYear(dto.getPublishedYear());
        existing.setAvailable(dto.getAvailable());

        return toResponseDTO(bookRepository.save(existing));
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        findOrThrow(id); // ensures 404 if not found before attempting delete
        bookRepository.deleteById(id);
    }

    // =========================================================================
    // SEARCH
    // =========================================================================

    @Override
    public List<Bookresponsedto> searchBooks(String author, String title) {
        boolean hasAuthor = StringUtils.hasText(author);
        boolean hasTitle  = StringUtils.hasText(title);

        List<Book> results;

        if (hasAuthor && hasTitle) {
            results = bookRepository.findByAuthorAndTitleContainingIgnoreCase(author, title);
        } else if (hasAuthor) {
            results = bookRepository.findByAuthorContainingIgnoreCase(author);
        } else if (hasTitle) {
            results = bookRepository.findByTitleContainingIgnoreCase(title);
        } else {
            results = bookRepository.findAll();
        }

        return results.stream().map(this::toResponseDTO).collect(Collectors.toList());
    }

    // =========================================================================
    // PRIVATE HELPERS
    // =========================================================================

    /** Fetches a Book by ID or throws ResourceNotFoundException (→ 404) */
    private Book findOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new Resourcenotfoundexception("Book", "id", id));
    }

    /** DTO → Entity (used on CREATE; id is null, DB generates it) */
    private Book toEntity(Bookrequestdto dto) {
        return Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publishedYear(dto.getPublishedYear())
                .available(dto.getAvailable())
                .build();
    }

    /** Entity → Response DTO (used in every response) */
    private Bookresponsedto toResponseDTO(Book book) {
        return Bookresponsedto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .available(book.getAvailable())
                .build();
    }
}