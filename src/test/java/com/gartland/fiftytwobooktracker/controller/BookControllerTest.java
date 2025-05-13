package com.gartland.fiftytwobooktracker.controller;

import com.gartland.fiftytwobooktracker.model.Book;
import com.gartland.fiftytwobooktracker.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the BookController class.
 * <p>
 * Verifies CRUD operations: creating, retrieving, and deleting books via the REST endpoints.
 */
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests creating a new book via POST /api/books endpoint.
     * Verifies that a CREATED status and the saved book are returned.
     */
    @Test
    public void testCreateBook() {
        // Arrange
        String title = "Test Title";
        String author = "Test Author";
        String status = "READING";
        Book saved = Book.builder()
                .title(title)
                .author(author)
                .status(Book.Status.READING)
                .build();
        when(bookService.saveBook(any(Book.class), any(MultipartFile.class))).thenReturn(saved);
        MultipartFile mockFile = mock(MultipartFile.class);

        // Act
        ResponseEntity<Book> response = bookController.createBook(title, author, status, mockFile);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(saved, response.getBody());
        verify(bookService, times(1)).saveBook(any(Book.class), any(MultipartFile.class));
    }

    /**
     * Tests retrieving all books via GET /api/books endpoint.
     * Verifies that an OK status and the list of books are returned.
     */
    @Test
    public void testGetAllBooks() {
        // Arrange
        List<Book> list = new ArrayList<>();
        list.add(Book.builder().title("A").author("X").status(Book.Status.COMPLETED).build());
        list.add(Book.builder().title("B").author("Y").status(Book.Status.READING).build());
        when(bookService.getAllBooks()).thenReturn(list);

        // Act
        ResponseEntity<List<Book>> response = bookController.getAllBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(list, response.getBody());
        verify(bookService, times(1)).getAllBooks();
    }

    /**
     * Tests retrieving a book by ID when it exists via GET /api/books/{id} endpoint.
     * Verifies that an OK status and the book are returned.
     */
    @Test
    public void testGetBookById_Found() {
        // Arrange
        UUID id = UUID.randomUUID();
        Book book = Book.builder().title("Title").author("Author").status(Book.Status.COMPLETED).build();
        when(bookService.getBookById(id)).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<Book> response = bookController.getBookById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(book, response.getBody());
        verify(bookService, times(1)).getBookById(id);
    }

    /**
     * Tests retrieving a book by ID when it does not exist via GET /api/books/{id} endpoint.
     * Verifies that a NOT_FOUND status is returned.
     */
    @Test
    public void testGetBookById_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(bookService.getBookById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.getBookById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, times(1)).getBookById(id);
    }

    /**
     * Tests deleting a book by ID when it exists via DELETE /api/books/{id} endpoint.
     * Verifies that a NO_CONTENT status is returned and deleteBook is invoked.
     */
    @Test
    public void testDeleteBook_Found() {
        // Arrange
        UUID id = UUID.randomUUID();
        Book book = Book.builder().title("Z").author("W").status(Book.Status.READING).build();
        when(bookService.getBookById(id)).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(id);
    }

    /**
     * Tests deleting a book by ID when it does not exist via DELETE /api/books/{id} endpoint.
     * Verifies that a NOT_FOUND status is returned and deleteBook is not invoked.
     */
    @Test
    public void testDeleteBook_NotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(bookService.getBookById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(bookService, never()).deleteBook(any(UUID.class));
    }
}
