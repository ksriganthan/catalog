package org.example.catalog.data;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Author {
    @Id
    @GeneratedValue
    private int authorId;
    @Column(name = "Name", nullable = false)
    private String name;
    @Column(name = "Nachname", nullable = false)
    private String surname;

    @ManyToMany()
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "authorId"),
            inverseJoinColumns = @JoinColumn(name = "ISBN"))
    @JsonBackReference
    private List<Book> books = new ArrayList<>();

    public Author(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Author() {

    }

    public int getAuthorId() {
        return this.authorId;
    }

    public void setAuthorId ( int authorId){
        this.authorId = authorId;
    }

    public String getName () {
        return this.name;
    }

    public void setName (String name){
        this.name = name;
    }

    public String getSurname () {
        return this.surname;
    }

    public void setSurname (String surname){
        this.surname = surname;
    }

    public List<Book> getBooks () {
        return books;
    }

    public void setBooks (List < Book > books) {
        this.books = books;
    }

    @Override
    public String toString () {
        return "Author [name=" + this.name + ", surname=" + this.surname + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authorId);
    }
}
