package com.daniel.library_management.repository;

import com.daniel.library_management.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jndi.JndiTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository class for Book database operations using JDBC.
 * 
 * <p>This class handles all database interactions for Book entities,
 * including CRUD operations, search, and bulk operations. Uses
 * Spring's JdbcTemplate for efficient database access.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Repository
public class BookRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * RowMapper implementation for mapping database rows to Book objects.
     */
    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Book.builder()
                .id(rs.getString("id"))
                .title(rs.getString("title"))
                .author(rs.getString("author"))
                .isbn(rs.getString("isbn"))
                .publicationYear(rs.getInt("publication_year"))
                .available(rs.getBoolean("available"))
                .addedDate(rs.getDate("added_date").toLocalDate())
                .build();
        }
    }
    
    /**
     * Saves a new book to the database.
     * 
     * @param book The book to save
     * @return The saved book with generated ID
     * @throws IllegalArgumentException if book is null
     */
    @Transactional
    public Book save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        String sql = """
            INSERT INTO books (id, title, author, isbn, publication_year, available, added_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        jdbcTemplate.update(sql,
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublicationYear(),
            book.isAvailable(),
            book.getAddedDate()
        );
        
        return book;
    }
    
    /**
     * Finds a book by its ID.
     * 
     * @param id The UUID of the book
     * @return Optional containing the book if found
     */
    public Optional<Book> findById(String id) {
        String sql = "SELECT * FROM books WHERE id = ?";
        try {
            Book book = jdbcTemplate.queryForObject(sql, new BookRowMapper(), id);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Finds a book by its ISBN.
     * 
     * @param isbn The ISBN to search for
     * @return Optional containing the book if found
     */
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "SELECT * FROM books WHERE isbn = ?";
        try {
            Book book = jdbcTemplate.queryForObject(sql, new BookRowMapper(), isbn);
            return Optional.ofNullable(book);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Retrieves all books from the database.
     * 
     * @return List of all books
     */
    public List<Book> findAll() {
        String sql = "SELECT * FROM books ORDER BY title";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }
    
    /**
     * Updates an existing book.
     * 
     * @param book The book with updated information
     * @return The updated book
     */
    @Transactional
    public Book update(Book book) {
        String sql = """
            UPDATE books 
            SET title = ?, author = ?, isbn = ?, publication_year = ?, available = ?
            WHERE id = ?
            """;
        
        jdbcTemplate.update(sql,
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublicationYear(),
            book.isAvailable(),
            book.getId()
        );
        
        return book;
    }
    
    /**
     * Deletes a book by its ID.
     * 
     * @param id The UUID of the book to delete
     * @return true if deleted successfully
     */
    @Transactional
    public boolean deleteById(String id) {
        String sql = "DELETE FROM books WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0;
    }
    
    /**
     * Searches for books by title or author (case-insensitive).
     * 
     * @param keyword The search keyword
     * @return List of matching books
     */
    public List<Book> search(String keyword) {
        String sql = """
            SELECT * FROM books 
            WHERE LOWER(title) LIKE LOWER(?) 
               OR LOWER(author) LIKE LOWER(?)
            ORDER BY title
            """;
        String searchPattern = "%" + keyword + "%";
        return jdbcTemplate.query(sql, new BookRowMapper(), searchPattern, searchPattern);
    }
    
    /**
     * Finds all available books.
     * 
     * @return List of available books
     */
    public List<Book> findAvailableBooks() {
        String sql = "SELECT * FROM books WHERE available = true ORDER BY title";
        return jdbcTemplate.query(sql, new BookRowMapper());
    }
    
    /**
     * Updates the availability status of a book.
     * 
     * @param bookId The book ID
     * @param available The new availability status
     */
    @Transactional
    public void updateAvailability(String bookId, boolean available) {
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        jdbcTemplate.update(sql, available, bookId);
    }
    
    /**
     * Gets the total count of books in the database.
     * 
     * @return Total book count
     */
    public long count() {
        String sql = "SELECT COUNT(*) FROM books";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    
    /**
     * Bulk inserts multiple books using batch update.
     * 
     * @param books List of books to insert
     * @return Number of books inserted
     */
    @Transactional
    public int batchSave(List<Book> books) {
        String sql = """
            INSERT INTO books (id, title, author, isbn, publication_year, available, added_date)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        List<Object[]> batchArgs = books.stream()
            .map(book -> new Object[]{
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.isAvailable(),
                book.getAddedDate()
            })
            .toList();
        
        int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
        return results.length;
    }
}