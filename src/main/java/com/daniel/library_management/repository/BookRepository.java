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

    @Query("select b from Book b where lower(b.title) like lower(concat('%', :keyword, '%')) "
        + "or lower(b.author) like lower(concat('%', :keyword, '%'))")
    List<Book> search(@Param("keyword")String keyword);

    Optional<Book> findByIsbn(String isbn);
}