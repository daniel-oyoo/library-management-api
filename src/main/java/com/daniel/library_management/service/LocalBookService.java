package com.daniel.library_management.service;

import com.daniel.library_management.exception.DuplicateResourceException;
import com.daniel.library_management.exception.ResourceNotFoundException;
import com.daniel.library_management.model.Book;
import com.daniel.library_management.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class LocalBookService {

    @Autowired
    private BookRepository bookRepository;

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
        existingBook.setSource(existingBook.getSource() == null ? "local" : existingBook.getSource());

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
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllBooks();
        }
        return bookRepository.search(keyword);
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
}
