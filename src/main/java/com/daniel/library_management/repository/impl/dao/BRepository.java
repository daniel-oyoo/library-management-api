package com.daniel.library_management.repository.impl.dao;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Book;

@Repository
public interface BRepository  extends JpaRepository<Book,String> {
    //single book
    Book save(Book book);
    //find all books
    List<Book> findAll();
    //find by id
    Optional<Book>findByid(String id);
    /* 
    //update book
    Book updateBook(String id);
    //delete book
    void delete(String id);
    //search
    List<Book> searchBook(String keyWord);
    Optional<Book> findByIsbn(String isbn);
    /*Book update(Book existingBook);*/
      /*
    @Query(value="SELECT * FROM book")
    List<Book> findAvailableBooks();
   int batchSave(List<Book> books);*/
   
}
