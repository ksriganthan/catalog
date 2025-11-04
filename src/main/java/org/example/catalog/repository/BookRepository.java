package org.example.catalog.repository;

import org.example.catalog.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    Book findByTitle(String title);
    List<Book> findByAuthors_Surname(String surname);
    List<Book> findByAuthors_NameAndAuthors_Surname(String name, String surname);
}
