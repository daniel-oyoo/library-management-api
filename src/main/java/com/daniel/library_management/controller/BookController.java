package com.daniel.library_management.controller;

import com.daniel.library_management.model.Book;
import com.daniel.library_management.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController                // Marks this as REST controller (handles HTTP requests)
@RequestMapping("/api/books")      // All endpoints in this controller start with /books
public class BookController {
    
    @Autowired                // Spring injects the BookService automatically
    private BookService bookService;
    
    // GET /books - View all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);  // Returns 200 OK with books list
    }
    
    // GET /books/{id} - View single book
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.notFound().build();  // Returns 404 Not Found
        }
        return ResponseEntity.ok(book);  // Returns 200 OK with book
    }
    
    // POST /books - Add new book (your /books/add)
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book) {
        Book newBook = bookService.addBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBook);  // Returns 201 Created
    }
    
    // PUT /books/{id} - Update book (your /book/edit/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Book updatedBook = bookService.updateBook(id, bookDetails);
        if (updatedBook == null) {
            return ResponseEntity.notFound().build();  // Returns 404
        }
        return ResponseEntity.ok(updatedBook);  // Returns 200 OK
    }
    
    // DELETE /books/{id} - Delete book (your /book/delete/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();  // Returns 404
        }
        return ResponseEntity.noContent().build();  // Returns 204 No Content
    }
    
    // GET /books/search?q=keyword - Search books
    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String q) {
        List<Book> results = bookService.searchBooks(q);
        return ResponseEntity.ok(results);
    }
}