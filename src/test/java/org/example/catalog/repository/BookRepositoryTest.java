package org.example.catalog.repository;

import org.example.catalog.PostgresContainerTest;
import org.example.catalog.data.Author;
import org.example.catalog.data.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // damit BeforeAll nicht static sein muss
public class BookRepositoryTest extends PostgresContainerTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @BeforeAll
     void SetUp() {
        Author a1 = new Author("Joanne K.", "Rowling");
        Author a2 = new Author("Stephen", "King");
        Author a3 = new Author("Sebastian", "Fitzek");
        Author a4 = new Author("Guillaume", "Musso");

        Book b1 = new Book("9783551557414", "Harry Potter und der Stein der Weisen", "Harry Potter Teil 1");
        Book b2 = new Book("9783453435773", "ES", "Clown der Kinder frisst");
        Book b3 = new Book("9783426281758", "Der Nachbar", "Frau leidet an Monophobie, Nachbar verfolgt sie");
        Book b4 = new Book("9783492309257", "Nacht im Central Park", "Polizistin und Jazzpianist werden entführt");
        Book b5 = new Book("9783426519486", "Mimik", "Frau leidet an Gedächtnisverlust und versucht den Mord an Paul zu verhindern");

        b1.getAuthors().add(a1);
        b2.getAuthors().add(a2);
        b3.getAuthors().add(a3);
        b4.getAuthors().add(a4);
        b5.getAuthors().add(a3);

        a1.getBooks().add(b1);
        a2.getBooks().add(b2);
        a3.getBooks().add(b3);
        a4.getBooks().add(b4);
        a3.getBooks().add(b5);

        bookRepository.save(b1);
        bookRepository.save(b2);
        bookRepository.save(b3);
        bookRepository.save(b4);
        bookRepository.save(b5);

        authorRepository.save(a1);
        authorRepository.save(a2);
        authorRepository.save(a3);
        authorRepository.save(a4);
    }


    @Test
    void testFindOneBookById() {
        var found = bookRepository.findById("9783426281758"); // Optional-Objekt
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Der Nachbar");
    }

    @Test
    void findByInvalidISBNReturnsEmptyOptional() {
        var found = bookRepository.findById("9783420281758"); // Optional-Objekt
        assertThat(found).isNotPresent();
    }


    @Test
    void findBookByTitle() {
        Book found = bookRepository.findByTitle("ES");
        assertNotNull(found);
        assertEquals("ES", found.getTitle());
    }

    @Test
    void findTwoBookFromAuthorByAuthorName() {
        List<Book> lists = bookRepository.findByAuthors_Surname("Fitzek");
        assertThat(lists).isNotEmpty();
        assertThat(lists).hasSize(2);
        assertThat(lists)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder("Der Nachbar", "Mimik");
    }

    @Test
    void findByUnknownAuthorReturnsEmptyList() {
        var result = bookRepository.findByAuthors_NameAndAuthors_Surname("Unknown", "Person");
        assertThat(result).isEmpty();
    }

}
