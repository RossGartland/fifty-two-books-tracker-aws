package com.gartland.fiftytwobooktracker.service;

import com.gartland.fiftytwobooktracker.model.Book;
import com.gartland.fiftytwobooktracker.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing book data and file uploads.
 * Handles saving book information to the database and uploading images to S3.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final S3Service s3Service;

    /**
     * Constructs a BookService with the given repository and S3 service.
     *
     * @param bookRepository The repository for managing book data.
     * @param s3Service The service for uploading images to S3.
     */
    public BookService(BookRepository bookRepository, S3Service s3Service) {
        this.bookRepository = bookRepository;
        this.s3Service = s3Service;
    }

    /**
     * Save a new book with an optional image upload.
     *
     * @param book The book entity to save.
     * @param imageFile The image file to upload to S3.
     * @return The saved book entity with S3 URL (if applicable).
     */
    public Book saveBook(Book book, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            String s3Url = s3Service.uploadFile(imageFile);
            book.setImageUrl(s3Url);
        }
        return bookRepository.save(book);
    }

    /**
     * Retrieves all books from the database.
     *
     * @return A list of all books.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Retrieves a book by its ID.
     *
     * @param id The unique identifier of the book.
     * @return An Optional containing the book if found.
     */
    public Optional<Book> getBookById(UUID id) {
        return bookRepository.findById(id);
    }

    /**
     * Deletes a book by its ID.
     *
     * @param id The unique identifier of the book to be deleted.
     */
    public void deleteBook(UUID id) {
        bookRepository.deleteById(id);
    }
}
