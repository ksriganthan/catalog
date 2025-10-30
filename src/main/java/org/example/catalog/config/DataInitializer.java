package org.example.catalog.config;

/*
Klasse lädt zu Beginn immer Beispiel-Daten in die H2-Datenbank
 */

import jakarta.annotation.PostConstruct;
import org.example.catalog.data.Author;
import org.example.catalog.data.Book;
import org.example.catalog.repository.AuthorRepository;
import org.example.catalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    public DataInitializer(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @PostConstruct
    public void init(){
        if (bookRepository.count() == 0) {

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

            System.out.println("Data initializer erfolgreich geladen");
        } else {
            System.out.println("Daten wurden nicht geladen, Datenbank enthält bereits Bücher");
        }
    }
}
