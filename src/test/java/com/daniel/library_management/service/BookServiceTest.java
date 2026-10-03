package com.daniel.library_management.service;

import com.daniel.library_management.exception.DuplicateResourceException;
import com.daniel.library_management.model.Book;
import com.daniel.library_management.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void addBookSetsIdentityAndAvailabilityDefaults() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);
        book.setId(null);
        book.setAvailable(false);
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book saved = bookService.addBook(book);

        assertThat(saved.getId()).isNotBlank();
        assertThat(saved.getAddedDate()).isNotNull();
        assertThat(saved.isAvailable()).isTrue();
        verify(bookRepository).save(book);
    }

    @Test
    void addBookRejectsDuplicateIsbn() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> bookService.addBook(book))
            .isInstanceOf(DuplicateResourceException.class)
            .hasMessageContaining("9780132350884");
        verify(bookRepository, never()).save(book);
    }
    @Test
    void newBookDefaultsToLocalSource() {
        Book book = new Book("Clean Code", "Robert C. Martin", "9780132350884", 2008);

        assertThat(book.getSource()).isEqualTo("local");
    }
}
