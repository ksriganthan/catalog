package org.example.catalog.data;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToMany(mappedBy = "books")
    @JsonManagedReference
    private List<Author> authors = new ArrayList<>();

    public Book(String ISBN, String title, String description) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
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
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    @Override
    public String toString() {
        return "Book [ISBN=" + this.ISBN + ", title=" + this.title + ", description=" + this.description + ", authors=" + this.authors + "]";
    }

}
