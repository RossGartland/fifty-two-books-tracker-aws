package com.gartland.fiftytwobooktracker.controller;

import com.gartland.fiftytwobooktracker.model.Book;
import com.gartland.fiftytwobooktracker.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing books.
 * Supports creating, retrieving, updating, and deleting book records.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    /**
     * Constructs a BookController with the given BookService.
     *
     * @param bookService The service handling book operations.
     */
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Endpoint to create a new book with an image upload.
     *
     * @param title The title of the book.
     * @param author The author of the book.
     * @param status The reading status of the book.
     * @param imageFile The image file associated with the book.
     * @return The created book entity.
     */
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Book> createBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("status") String status,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Book book = Book.builder()
                .title(title)
                .author(author)
                .status(Book.Status.valueOf(status))
                .build();

        Book savedBook = bookService.saveBook(book, imageFile);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all books.
     *
     * @return A list of all books.
     */
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve a book by its ID.
     *
     * @param id The UUID of the book.
     * @return The book entity if found, 404 Not Found otherwise.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable UUID id) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(book.get(), HttpStatus.OK);
    }

    /**
     * Endpoint to delete a book by its ID.
     *
     * @param id The UUID of the book to be deleted.
     * @return 204 No Content if deleted successfully, 404 Not Found if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        Optional<Book> book = bookService.getBookById(id);

        if (book.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
