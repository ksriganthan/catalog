package org.example.catalog.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    @Id
    private int authorId;
    @Column(name = "Name", nullable = false)
    private String name;
    @Column(name = "Nachname", nullable = false)
    private String surname;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Damit keine Endlosschleife bei JSON entsteht -- noch anschauen!
    private List<Book> books = new ArrayList<>();

    public Author(int authorId, String name, String surname) {
        this.authorId = authorId;
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


}
