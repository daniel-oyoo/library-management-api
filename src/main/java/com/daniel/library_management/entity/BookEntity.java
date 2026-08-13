package com.daniel.library_management.entity;

import java.time.LocalDate;

import lombok.Builder;

public class BookEntity {
     /**
     * Unique identifier for the book using UUID format.
     * Example: "123e4567-e89b-12d3-a456-426614174000"
     */
    private String id;
    
    /**
     * The title of the book. Cannot be null or empty.
     */
    private String title;
    
    /**
     * The author(s) of the book.
     */
    private String author;
    
    /**
     * International Standard Book Number (ISBN-10 or ISBN-13 format).
     * Must be unique across all books.
     */
    private String isbn;
    
    /**
     * The year the book was published.
     * Valid range: 1000 to current year + 1.
     */
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
    private LocalDate addedDate;   
}
