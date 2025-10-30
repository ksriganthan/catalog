package org.example.catalog.service;

import org.example.catalog.data.Author;
import org.example.catalog.data.Book;
import org.example.catalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    private BookRepository bookRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = Mockito.mock(BookRepository.class);

        bookService = new BookService();

        try {
            var field = BookService.class.getDeclaredField("bookRepository");
            field.setAccessible(true);
            field.set(bookService, bookRepository);
        } catch (Exception e){
            throw new RuntimeException("Fehler beim Einfügen von Mock BookRepository ",e);
        }

        Author a1 = new Author(1, "Joanne K.", "Rowling");
        Author a2 = new Author(2, "Stephen", "King");
        Author a3 = new Author(3, "Sebastian", "Fitzek");
        Author a4 = new Author(4, "Guillaume", "Musso");

        List<Book> testBooks = List.of(
                new Book("9783551557414", "Harry Potter und der Stein der Weisen", "Harry Potter Teil 1", a1),
                new Book("9783453435773", "ES", "Clown der Kinder frisst", a2),
                new Book("9783426281758", "Der Nachbar", "Frau leidet an Monophobie, Nachbar verfolgt sie", a3),
                new Book("9783492309257", "Nacht im Central Park", "Polizistin und Jazzpianist werden entführt", a4),
                new Book("9783426519486", "Mimik", "Frau leidet an Gedächtnisverlust und versucht den Mord an Paul zu verhindern", a3)
        );

        when(bookRepository.findAll()).thenReturn(testBooks);
    }

    @Test
    void findBookByTitle() {
        var result = bookService.searchBooks(List.of("Harry"));
        assertEquals(1, result.size());
        assertEquals("Harry Potter und der Stein der Weisen", result.get(0).getTitle());
    }

    @Test
    void findBookByAuthor() {
        var result = bookService.searchBooks(List.of("King"));
        assertEquals(1, result.size());
        assertEquals("ES", result.get(0).getTitle());
    }

    @Test // Test mit Author der zwei Bücher hat
    void findBookByAuthorTwoBooks() {
        var result = bookService.searchBooks(List.of("Fitzek"));
        assertEquals(2, result.size());
        assertEquals("Sebastian", result.get(0).getAuthor().getName());
    }

    @Test
    void findBookByAuthorAndTitle() {
        var result = bookService.searchBooks(List.of("Nachbar", "Fitzek"));
        assertEquals(1, result.size());
        assertEquals("Der Nachbar", result.get(0).getTitle());

    }

    @Test
    void case_insensitive() {
        var result = bookService.searchBooks(List.of("harry"));
        assertEquals(1, result.size());
        assertEquals("Harry Potter und der Stein der Weisen", result.get(0).getTitle());
    }

    @Test
    void various_keywords() {
        var result = bookService.searchBooks(Arrays.asList("Central", "Nacht"));
        assertEquals(1, result.size());
        assertEquals("Nacht im Central Park", result.get(0).getTitle());
    }

    @Test
    void unknown_keyword_empty_list() {
        var result = bookService.searchBooks(List.of("Narnia"));
        assertEquals(0, result.size());
    }

    @Test
    void empty_keywords_returns_all() {
        var result = bookService.searchBooks(List.of());
        assertEquals(5, result.size());
    }
}
