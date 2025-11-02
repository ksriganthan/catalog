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

    public BookService() {
        this.bookRepository = bookRepository;
    }

    // Suchmethode mit UND-Verknüpfung (case-insensitive) und verschiedene Keywords
    public List<Book> searchBooks(List<String> keywords) {
        List<Book> result = bookRepository.findAll();
        if(keywords != null) {
            for (String w : keywords) {
                String word = w.toLowerCase();
                result = result.stream()
                        .filter(b -> {
                            String allText =
                                    (b.getTitle() + " " + b.getDescription() + b.getAuthors().toString()).toLowerCase();
                            return allText.contains(word);
                        }).toList();
            }
        }
        return result;
    }

}
