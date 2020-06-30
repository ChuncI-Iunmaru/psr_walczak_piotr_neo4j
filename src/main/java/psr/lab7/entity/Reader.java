package psr.lab7.entity;

import org.neo4j.ogm.annotation.*;

import java.util.HashSet;
import java.util.Set;

@NodeEntity(label = "Reader")
public class Reader {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    @Property(name = "surname")
    private String surname;

    public Reader() {
    }

    public Reader(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return String.format("Nr. karty: %d - %s %s", id, name, surname);
    }

    @Relationship(type = "BORROWED")
    private Set<Book> books = new HashSet<>();

    public Set<Book> getBooks() {
        return books;
    }

    public boolean addBook(Book book) {
        return books.add(book);
    }

    public boolean removeBook(Book book) {
        return books.remove(book);
    }
}
