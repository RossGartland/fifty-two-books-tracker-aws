package com.gartland.fiftytwobooktracker.helper;

import com.gartland.fiftytwobooktracker.model.Book;
import com.gartland.fiftytwobooktracker.model.Book.Status;
import com.gartland.fiftytwobooktracker.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DataLoader to populate the database with initial book data.
 */
@Component
public class BookDataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;

    /**
     * Constructs the DataLoader with the repository.
     *
     * @param bookRepository The repository to manage book data.
     */
    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Populates the database with sample book records.
     *
     * @param args Command line arguments.
     */
    @Override
    public void run(String... args) {

        if (bookRepository.count() == 0) {
            bookRepository.save(Book.builder()
                    .title("Atomic Habits")
                    .author("James Clear")
                    .status(Status.TO_READ)
                    .imageUrl("https://s3.amazonaws.com/fifty-two-books-tracker-bucket/placeholder1.jpg")
                    .build());

            bookRepository.save(Book.builder()
                    .title("Deep Work")
                    .author("Cal Newport")
                    .status(Status.READING)
                    .imageUrl("https://s3.amazonaws.com/fifty-two-books-tracker-bucket/placeholder2.jpg")
                    .build());

            bookRepository.save(Book.builder()
                    .title("The Power of Now")
                    .author("Eckhart Tolle")
                    .status(Status.COMPLETED)
                    .imageUrl("https://s3.amazonaws.com/fifty-two-books-tracker-bucket/placeholder3.jpg")
                    .build());
        }
    }
}
