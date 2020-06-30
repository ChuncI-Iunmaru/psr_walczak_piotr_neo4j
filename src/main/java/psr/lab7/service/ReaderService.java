package psr.lab7.service;

import org.neo4j.ogm.session.Session;
import psr.lab7.entity.Book;
import psr.lab7.entity.Reader;

import java.util.HashMap;
import java.util.Map;

public class ReaderService extends GenericService<Reader> {


    public ReaderService(Session session) {
        super(session);
    }

    @Override
    Class<Reader> getEntityType() {
        return Reader.class;
    }

    public boolean returnBook(Reader reader, Book book){
        if (reader.removeBook(book)){
            session.save(reader);
            return true;
        } else return false;
    }

    public boolean borrowBook(Reader reader, Book book){
        if (reader.addBook(book)){
            session.save(reader);
            return true;
        } else return false;
    }
}
