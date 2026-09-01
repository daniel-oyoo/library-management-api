package com.daniel.library_management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.daniel.library_management.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book,String>{

    @Query(value="SELECT * FROM books WHERE title LIKE \'%keyword%\' OR \'author\' LIKE \'%keyword%\'",nativeQuery=true)
    List<Book> search(@Param("keyword")String keyword);

    Optional<Book> findByIsbn(String isbn);
}