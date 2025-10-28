package org.example.catalog.config;

/*
Klasse lädt zu Beginn immer Beispiel-Daten in die H2-Datenbank
 */

import jakarta.annotation.PostConstruct;
import org.example.catalog.data.Author;
import org.example.catalog.data.Book;
import org.example.catalog.repository.BookRepository;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final BookRepository bookRepository;

    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    @PostConstruct
    public void init(){
        if (bookRepository.count() == 0) {

            Author a1 = new Author(1, "Joanne K.", "Rowling");
            Author a2 = new Author(2, "Stephen", "King");
            Author a3 = new Author(3, "Sebastian", "Fitzek");
            Author a4 = new Author(4, "Guillaume", "Musso");

            Book b1 = new Book("9783551557414", "Harry Potter und der Stein der Weisen", "Harry Potter Teil 1", a1);
            Book b2 = new Book("9783453435773", "ES", "Clown der Kinder frisst", a2);
            Book b3 = new Book("9783426281758", "Der Nachbar", "Frau leidet an Monophobie, Nachbar verfolgt sie", a3);
            Book b4 = new Book("9783492309257", "Nacht im Central Park", "Polizistin und Jazzpianist werden entführt", a4);
            Book b5 = new Book("9783426519486", "Mimik", "Frau leidet an Gedächtnisverlust und versucht den Mord an Paul zu verhindern", a3);

            bookRepository.save(b1);
            bookRepository.save(b2);
            bookRepository.save(b3);
            bookRepository.save(b4);
            bookRepository.save(b5);

            System.out.println("Data initializer erfolgreich geladen");
        } else {
            System.out.println("Daten wurden nicht geladen, Datenbank enthält bereits Bücher");
        }
    }
}
