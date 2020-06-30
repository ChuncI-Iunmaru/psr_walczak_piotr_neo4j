package psr.lab7.entity;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

@NodeEntity(label = "Book")
public class Book {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "title")
    private String title;

    @Property(name = "relase_date")
    private String releaseDate;

    @Property(name = "publisher")
    private String publisher;

    public Book() {
    }

    public Book(String title, String releaseDate, String publisher) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.publisher = publisher;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format("[%d] '%s', wyd. %s, %s", id, title, publisher, releaseDate);
    }
}
