package psr.lab7.service;

public interface Service<T> {

    T getById(Long id);

    Iterable<T> getAll();

    void delete(Long id);

    void deleteAll();

    void createOrUpdate(T object);
}
