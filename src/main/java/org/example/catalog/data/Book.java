package org.example.catalog.data;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    private String ISBN;
    @Column(name = "Titel", nullable = false)
    private String title;

    @Column(name = "Beschreibung", nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id")  // Fremdschlüssel auf Author
    private Author author;

    public Book(String ISBN, String title, String description, Author author) {
        this.ISBN = ISBN;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public Book() {

    }

    public Author getAuthor() {
        return author;
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


    @Override
    public String toString() {
        return "Book [ISBN=" + this.ISBN + ", title=" + this.title + ", description=" + this.description + ", authors=" + this.author + "]";
    }

}
