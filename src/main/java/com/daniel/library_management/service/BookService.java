package com.daniel.library_management.service;

import com.daniel.library_management.exception.DuplicateResourceException;
import com.daniel.library_management.exception.ResourceNotFoundException;
import com.daniel.library_management.model.Book;
import com.daniel.library_management.repository.BookRepository;
import com.daniel.library_management.repository.impl.dao.BRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for Book business logic.
 * 
 * <p>This service handles all business operations related to books,
 * including creation, updates, deletion, and searching. All operations
 * are transactional to ensure data consistency.</p>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Service
public class BookService {
    
    @Autowired
    private BRepository bookRepository;
    
    /**
     * Adds a new book to the library.
     * 
     * @param book The book to add
     * @return The added book with generated ID
     * @throws IllegalArgumentException if book is null
     * @throws DuplicateResourceException if book with same ISBN exists
     */
    @Transactional
    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        // Check for duplicate ISBN
        if (book.getIsbn() != null
        // && bookRepository.findByIsbn(book.getIsbn()).isPresent()
        ) {
            throw new DuplicateResourceException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        // Set default values
        if (book.getId() == null) {
            book.setId(java.util.UUID.randomUUID().toString());
        }
        book.setAddedDate(LocalDate.now());
        book.setAvailable(true);
        
        return bookRepository.save(book);
    }
    
    /**
     * Retrieves all books in the library.
     * 
     * @return List of all books
     */
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    /**
     * Retrieves a book by its ID.
     * 
     * @param id The book ID (UUID)
     * @return The book
     * @throws ResourceNotFoundException if book not found
     */
    @Transactional(readOnly = true)
    public Book getBookById(String id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
    
    /**
     * Updates an existing book.
     * 
     * @param id The book ID
     * @param bookDetails The updated book details
     * @return The updated book
     * @throws ResourceNotFoundException if book not found
     */
    @Transactional
    public Book updateBook(String id, Book bookDetails) {
        Book existingBook = getBookById(id);
        
        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPublicationYear(bookDetails.getPublicationYear());
        
        /*return bookRepository.update(existingBook);*/
        return new Book();
    }
    
    /**
     * Deletes a book from the library.
     * 
     * @param id The book ID
     * @return true if deleted successfully
     * @throws ResourceNotFoundException if book not found
     */
    @Transactional
    public boolean deleteBook(String id) {
        if (!bookRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        //boolean deleteResults=
        bookRepository.deleteById(id);
        return true;
    }
    
    /**
     * Searches for books by title or author.
     * 
     * @param keyword The search keyword
     * @return List of matching books
     */
    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        //return bookRepository.searchBook(keyword);
        return bookRepository.findAll();
    }
    
    /**
     * Gets all available books.
     * 
     * @return List of available books
     */
    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll();
        //return bookRepository.findAvailableBooks();
    }
    
    /**
     * Generates a large number of random books for testing.
     * 
     * @param count Number of books to generate
     * @return Number of books generated
     */
    @Transactional
    public int generateRandomBooks(int count) {
        List<Book> books = DataGenerator.generateBooks(count);
        for(Book book : books){
            bookRepository.save(book);
        }
        /* 
        return bookRepository.batchSave(books);
        */
       return 1;//placeholder
    }
    
    /**
     * Gets total book count.
     * 
     * @return Total number of books
     */
    @Transactional(readOnly = true)
    public long getBookCount() {
        return bookRepository.count();
    }
}