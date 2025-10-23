package org.example.catalog.service;

import org.example.catalog.data.Book;
import org.example.catalog.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    // Suchmethode mit UND-Verknüpfung (case-insensitive)
    public List<Book> searchBooks(String keywordString) {
        // Eingabe: "felix microservice" → ["felix", "microservice"]
        String[] words = keywordString.toLowerCase().split("\\s+");

        // Alle Bücher laden (könnte man später optimieren)
        List<Book> result = bookRepository.findAll();

        // UND-Filter: Buch muss alle Wörter enthalten
        for (String w : words) {
            result = result.stream()
                    .filter(b -> (
                            (b.getTitle() + " " + b.getDescription() + " " +
                                    b.getAuthors().toString()).toLowerCase()
                    ).contains(w))
                    .toList();
        }

        return result;
    }

}
