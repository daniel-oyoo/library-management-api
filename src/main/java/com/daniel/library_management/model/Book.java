package com.daniel.library_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a Book entity in the library system.
 * 
 * <p>Books are the core inventory items managed by the library.
 * Each book has a unique UUID identifier and contains bibliographic
 * information such as title, author, ISBN, and publication year.</p>
 * 
 * <p><strong>Business Rules:</strong></p>
 * <ul>
 *   <li>Each book has a unique ID (UUID)</li>
 *   <li>ISBN must be unique across all books</li>
 *   <li>A book can be available or on loan</li>
 *   <li>Publication year must be reasonable (1000-2024)</li>
 * </ul>
 * 
 * @author Daniel
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="books")
public class Book {
    
    /**
     * Unique identifier for the book using UUID format.
     * Example: "123e4567-e89b-12d3-a456-426614174000"
     */
    @Id
    private String id;
    
    /**
     * The title of the book. Cannot be null or empty.
     */
    @NotBlank
    private String title;
    
    /**
     * The author(s) of the book.
     */
    @NotBlank
    private String author;
    
    /**
     * International Standard Book Number (ISBN-10 or ISBN-13 format).
     * Must be unique across all books.
     */
    @NotBlank
    private String isbn;
    
    /**
     * The year the book was published.
     * Valid range: 1000 to current year + 1.
     */
     @Column(name="publicationYear")
    @NotNull
    @Min(1000)
    private Integer publicationYear;
    
    /**
     * Indicates whether the book is currently available for borrowing.
     * Default value is true.
     */
    @Builder.Default
    private boolean available = true;
    
    /**
     * The date when the book was added to the library collection.
     * Automatically set to current date on creation.
     */
    @Column(name="addedDate")
    private LocalDate addedDate;

    /**
     * Source of the book data: local library records or Google Books.
     */
    @Builder.Default
    @Column(name="source", nullable = false)
    private String source = "local";

    /**
     * Creates a new book with auto-generated UUID and current date.
     * 
     * @param title Book title
     * @param author Book author
     * @param isbn ISBN number
     * @param publicationYear Year of publication
     */
    public Book(String title, String author, String isbn, Integer publicationYear) {
        this();
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.available = true;
        this.addedDate = LocalDate.now();
    }
}