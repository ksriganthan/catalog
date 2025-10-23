package org.example.catalog.data;

import jakarta.persistence.*;

@Entity
public class Author {
    @Id
    private int authorId;
    @Column(name = "Name", nullable = false)
    private String name;
    @Column(name = "Nachname", nullable = false)
    private String surname;

    @ManyToOne
    @JoinColumn(name = "book_isbn") // Fremdschlüssel in der Author-Tabelle
    private Book book;

    public Author(int authorId,String name, String surname) {
        this.authorId = authorId;
        this.name = name;
        this.surname = surname;
    }

    public Author() {

    }

    public int getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Author [name=" + this.name + ", surname=" + this.surname + "]";
    }
}
