package com.parikhsit.bookmanagement.repository;

import com.parikhsit.bookmanagement.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * BookRepository — Spring Data JPA repository for the Book entity.
 *
 * Extending JpaRepository<Book, Long> gives us for free:
 *   save(), findById(), findAll(), deleteById(), existsById(), count(), ...
 *
 * Spring Data auto-generates SQL from method names at runtime — no implementation needed.
 */
@Repository
public interface Bookrepository extends JpaRepository<Book, Long> {

    // ------------------------------------------------------------------
    // ISBN uniqueness checks
    // ------------------------------------------------------------------

    /** True if any book already has this ISBN (used on CREATE) */
    boolean existsByIsbn(String isbn);

    /** True if a DIFFERENT book (id != given id) has this ISBN (used on UPDATE) */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /** Find by exact ISBN */
    Optional<Book> findByIsbn(String isbn);

    // ------------------------------------------------------------------
    // Search methods — all case-insensitive, partial match
    // ------------------------------------------------------------------

    /**
     * SQL generated: WHERE LOWER(author) LIKE LOWER('%?%')
     * "Containing" = LIKE with % on both sides
     * "IgnoreCase" = wraps both sides in LOWER()
     */
    List<Book> findByAuthorContainingIgnoreCase(String author);

    /** SQL generated: WHERE LOWER(title) LIKE LOWER('%?%') */
    List<Book> findByTitleContainingIgnoreCase(String title);

    /**
     * Combined search — matches author AND title simultaneously.
     * Custom JPQL (uses entity field names, not column names).
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')) AND " +
           "LOWER(b.title)  LIKE LOWER(CONCAT('%', :title,  '%'))")
    List<Book> findByAuthorAndTitleContainingIgnoreCase(
            @Param("author") String author,
            @Param("title")  String title
    );

    /** Filter by availability status */
    List<Book> findByAvailable(Boolean available);
}