/**
 * Represents a book entity in the 52 Book Challenge application.
 * This entity stores information about a book, including its title, author,
 * reading status, image URL, and timestamps for creation and update.
 */
package com.gartland.fiftytwobooktracker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class representing a book record in the database.
 */
@Entity
@Table(name = "books")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    /**
     * Unique identifier for the book, generated as a UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Title of the book.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Author of the book.
     */
    @Column(nullable = false)
    private String author;

    /**
     * Status of the book in the reading challenge.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * URL of the book cover image stored in AWS S3.
     */
    @Column
    private String imageUrl;

    /**
     * Timestamp of when the book record was created.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp of when the book record was last updated.
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Method called before the entity is persisted for the first time.
     * Initializes the creation and update timestamps.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Method called before the entity is updated.
     * Updates the "updatedAt" timestamp.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Enumeration representing the reading status of a book.
     */
    public enum Status {
        TO_READ, READING, COMPLETED
    }
}