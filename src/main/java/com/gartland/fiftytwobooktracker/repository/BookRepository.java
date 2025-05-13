package com.gartland.fiftytwobooktracker.repository;


import com.gartland.fiftytwobooktracker.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Book entities.
 * Extends JpaRepository to provide basic CRUD operations and query methods.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, UUID> {}

