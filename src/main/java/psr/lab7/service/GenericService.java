package psr.lab7.service;

import org.neo4j.ogm.session.Session;

import java.util.HashMap;
import java.util.Map;

public abstract class GenericService<T> implements Service<T> {

        protected Session session;

        public GenericService(Session session){
            this.session = session;
        }

        @Override
        public T getById(Long id) {
            return session.load(getEntityType(), id);
        }

        @Override
        public Iterable<T> getAll() {
            return session.loadAll(getEntityType());
        }

        @Override
        public void delete(Long id) {
            session.delete(session.load(getEntityType(), id));
        }

        @Override
        public void deleteAll() {
            session.deleteAll(getEntityType());
        }

        @Override
        public void createOrUpdate(T entity) {
            session.save(entity);
        }

        abstract Class<T> getEntityType();

    public Iterable<Map<String, Object>> getByQuery(String query, HashMap<String, Object> params){
        System.out.println("Wykonywanie zapytania " + query);
        return session.query(query, params).queryResults();
    }
}
