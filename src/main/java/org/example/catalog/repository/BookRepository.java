package org.example.catalog.repository;

import org.example.catalog.data.Author;
import org.example.catalog.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
}
