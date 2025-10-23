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

    // Suchmethode mit UND-Verknüpfung (case-insensitive) und verschiedenen Keywords
    public List<Book> searchBooks(List<String>keywords) {
        List<Book> result = bookRepository.findAll();

        for (String w : keywords) {
            String word = w.toLowerCase();
            result = result.stream()
                    .filter(b -> (
                            (b.getTitle() + " " + b.getDescription() + " " +
                                    b.getAuthors().toString()).toLowerCase()
                    ).contains(w))
                    .toList();
        }
        // Es wird Keyword für Keyword gefiltert pro Runde
        return result;
    }

}
