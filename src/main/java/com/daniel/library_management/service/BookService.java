package com.daniel.library_management.service;

import com.daniel.library_management.model.Book;
import org.springframework.stereotype.Service;
import java.util.*;

@Service  // Marks this as a Spring Service (business logic component)
public class BookService {
    private Map<Long, Book> books = new HashMap<>();
    private Long nextId = 1L;
    
    // Add a new book
    public Book addBook(Book book) {
        book.setId(nextId++);
        book.setAddedDate(java.time.LocalDate.now());
        book.setAvailable(true);
        books.put(book.getId(), book);
        return book;
    }
    
    // Get all books
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    // Get book by ID
    public Book getBookById(Long id) {
        return books.get(id);
    }
    
    // Update book
    public Book updateBook(Long id, Book bookDetails) {
        Book book = books.get(id);
        if (book != null) {
            book.setTitle(bookDetails.getTitle());
            book.setAuthor(bookDetails.getAuthor());
            book.setIsbn(bookDetails.getIsbn());
            book.setPublicationYear(bookDetails.getPublicationYear());
        }
        return book;
    }
    
    // Delete book
    public boolean deleteBook(Long id) {
        return books.remove(id) != null;
    }
    
    // Search books by title or author
    public List<Book> searchBooks(String keyword) {
        return books.values().stream()
            .filter(book -> book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                           book.getAuthor().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
    }
}