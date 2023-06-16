package ua.lviv.iot.spring.first.rest.service;

import java.io.IOException;
import java.util.Collection;

public interface TemplateService<T, ID> {
    T getById(ID id);

    Collection<T> getAll();

    T create(T entity) throws IOException;

    T update(ID id, T entity) throws IOException;

    T delete(ID id) throws IOException;
}
