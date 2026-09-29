package com.daniel.library_management.service;

import com.daniel.library_management.client.GoogleBooksClient;
import com.daniel.library_management.client.dto.GoogleBookItem;
import com.daniel.library_management.client.dto.GoogleBooksResponse;
import com.daniel.library_management.exception.DuplicateResourceException;
import com.daniel.library_management.exception.ResourceNotFoundException;
import com.daniel.library_management.model.Book;
import com.daniel.library_management.repository.BookRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoogleBooksClient googleBooksClient;

    @Autowired
    private GoogleBookMapper googleBookMapper;

    @Value("${google.books.api-key:}")
    private String apiKey;

    @Transactional
    public Book addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getIsbn() != null && bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
            throw new DuplicateResourceException("Book with ISBN " + book.getIsbn() + " already exists");
        }

        if (book.getId() == null) {
            book.setId(java.util.UUID.randomUUID().toString());
        }
        if (book.getSource() == null || book.getSource().isBlank()) {
            book.setSource("local");
        }
        book.setAddedDate(LocalDate.now());
        book.setAvailable(true);

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBookById(String id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Transactional
    public Book updateBook(String id, Book bookDetails) {
        Book existingBook = getBookById(id);

        existingBook.setTitle(bookDetails.getTitle());
        existingBook.setAuthor(bookDetails.getAuthor());
        existingBook.setIsbn(bookDetails.getIsbn());
        existingBook.setPublicationYear(bookDetails.getPublicationYear());
        if (existingBook.getSource() == null || existingBook.getSource().isBlank()) {
            existingBook.setSource("local");
        }

        return bookRepository.save(existingBook);
    }

    @Transactional
    public boolean deleteBook(String id) {
        if (!bookRepository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword) {
        return searchBooks(keyword, "auto");
    }

    @Transactional(readOnly = true)
    public List<Book> searchBooks(String keyword, String source) {
        if (keyword == null || keyword.isBlank()) {
            return getAllBooks();
        }

        String normalizedSource = source == null ? "auto" : source.trim().toLowerCase();
        if ("local".equals(normalizedSource)) {
            return localSearch(keyword);
        }
        if ("google".equals(normalizedSource)) {
            try {
                List<Book> googleBooks = googleSearch(keyword);
                if (googleBooks != null && !googleBooks.isEmpty()) {
                    return googleBooks;
                }
            } catch (Exception ignored) {
                // Fall through to local lookup below.
            }
            return localSearch(keyword);
        }

        try {
            List<Book> googleBooks = googleSearch(keyword);
            if (googleBooks != null && !googleBooks.isEmpty()) {
                return googleBooks;
            }
        } catch (Exception ignored) {
            // Fallback to local library data when Google is unavailable.
        }

        return localSearch(keyword);
    }

    @Transactional(readOnly = true)
    public List<Book> getAvailableBooks() {
        return bookRepository.findAll();
    }

    @Transactional
    public int generateRandomBooks(int count) {
        List<Book> books = DataGenerator.generateBooks(count);
        return bookRepository.saveAll(books).size();
    }

    @Transactional(readOnly = true)
    public long getBookCount() {
        return bookRepository.count();
    }

    @CircuitBreaker(name = "googleBooks", fallbackMethod = "fallbackSearch")
    private List<Book> googleSearch(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return new ArrayList<>();
        }

        GoogleBooksResponse response = googleBooksClient.searchBooks(keyword.trim(), safeApiKey());
        if (response == null || response.getItems() == null || response.getItems().isEmpty()) {
            return new ArrayList<>();
        }

        List<Book> books = new ArrayList<>();
        for (GoogleBookItem item : response.getItems()) {
            Book mapped = googleBookMapper.toBook(item);
            if (mapped != null) {
                mapped.setSource("google");
                books.add(mapped);
            }
        }
        return books;
    }

    private List<Book> localSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.search(keyword).stream()
            .peek(book -> book.setSource("local"))
            .toList();
    }

    private List<Book> fallbackSearch(String keyword, Throwable throwable) {
        return localSearch(keyword);
    }

    private String safeApiKey() {
        return (apiKey == null || apiKey.isBlank()) ? null : apiKey;
    }
}