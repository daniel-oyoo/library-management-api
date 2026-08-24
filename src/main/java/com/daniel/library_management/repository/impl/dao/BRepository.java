package com.daniel.library_management.repository.impl.dao;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Book;

@Repository
public interface BRepository  extends JpaRepository<Book,String> {

    //single book-creating and saving
    Book save(Book book);

    //find all books
    List<Book> findAll();

    //find by id
    Optional<Book>findById(String id);

    //delete
    void deleteById(String id);


    List<Book> findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);

    @Query("SELECT * FROM books where keyword =: keyword")
    List<Book> searchBook(String keyword);

    List<Book> findAvailableBooks();
}
