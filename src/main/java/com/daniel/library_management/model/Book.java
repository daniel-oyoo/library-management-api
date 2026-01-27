package com.daniel.library_management.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data                    // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Lombok: Generates no-args constructor
@AllArgsConstructor      // Lombok: Generates all-args constructor
public class Book {
    private Long id;                    // Unique identifier for the book
    private String title;               // Book title
    private String author;              // Book author
    private String isbn;                // International Standard Book Number
    private Integer publicationYear;    // Year published
    private boolean available;          // Is book available for borrowing?
    private LocalDate addedDate;        // When book was added to library
}