package psr.lab7.service;

import org.neo4j.ogm.session.Session;
import psr.lab7.entity.Book;

public class BookService extends GenericService<Book> {
    public BookService(Session session) {
        super(session);
    }

    @Override
    Class<Book> getEntityType() {
        return Book.class;
    }
}
