package com.gartland.fiftytwobooktracker.service;

import com.gartland.fiftytwobooktracker.model.Book;
import com.gartland.fiftytwobooktracker.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BookService class.
 */
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private S3Service s3Service;

    @InjectMocks
    private BookService bookService;

    private Book testBook;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookId = UUID.randomUUID();
        testBook = Book.builder()
                .id(bookId)
                .title("Test Book")
                .author("Test Author")
                .status(Book.Status.TO_READ)
                .build();
    }

    /**
     * Test for saving a book without an image.
     */
    @Test
    void saveBook_ShouldSaveBook_WithoutImage() {
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.saveBook(testBook, null);

        assertEquals("Test Book", savedBook.getTitle());
        assertEquals("Test Author", savedBook.getAuthor());
        verify(s3Service, never()).uploadFile(any(MultipartFile.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Test for saving a book with an image upload.
     */
    @Test
    void saveBook_ShouldSaveBook_WithImage() {
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.getOriginalFilename()).thenReturn("test-image.jpg");
        when(s3Service.uploadFile(mockImage)).thenReturn("https://s3.amazonaws.com/fifty-two-books-tracker/test-image.jpg");
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        Book savedBook = bookService.saveBook(testBook, mockImage);

        assertEquals("https://s3.amazonaws.com/fifty-two-books-tracker/test-image.jpg", savedBook.getImageUrl());
        verify(s3Service, times(1)).uploadFile(mockImage);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    /**
     * Test for retrieving all books.
     */
    @Test
    void getAllBooks_ShouldReturnBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(testBook));

        assertThat(bookService.getAllBooks()).hasSize(1);
        assertEquals("Test Book", bookService.getAllBooks().get(0).getTitle());
        verify(bookRepository, times(2)).findAll();  // Called twice in this test
    }

    /**
     * Test for retrieving a book by its ID when it exists.
     */
    @Test
    void getBookById_ShouldReturnBook_WhenExists() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));

        Optional<Book> foundBook = bookService.getBookById(bookId);

        assertTrue(foundBook.isPresent());
        assertEquals("Test Book", foundBook.get().getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    /**
     * Test for retrieving a book by its ID when it does not exist.
     */
    @Test
    void getBookById_ShouldReturnEmpty_WhenNotExists() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Optional<Book> foundBook = bookService.getBookById(bookId);

        assertTrue(foundBook.isEmpty());
        verify(bookRepository, times(1)).findById(bookId);
    }

    /**
     * Test for deleting a book by its ID.
     */
    @Test
    void deleteBook_ShouldDeleteBook() {
        bookService.deleteBook(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }
}
