package org.example.catalog.data;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Book {
    @Id
    private String ISBN;
    @Column(name = "Titel", nullable = false)
    private String title;

    @Column(name = "Beschreibung", nullable = false)
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Author> authors = new ArrayList<>();

    public Book(String ISBN, String title, String description, List<Author> authors) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.authors = authors;
    }

    public Book() {

    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }


    public void addAuthor(Author author) {
        authors.add(author);
        if (author.getBook() != this) {
            author.setBook(this);
        }
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        if (author.getBook() == this) {
            author.setBook(null);
        }
    }

    @Override
    public String toString() {
        return "Book [ISBN=" + this.ISBN + ", title=" + this.title + ", description=" + this.description + ", authors=" + this.authors + "]";
    }



}
