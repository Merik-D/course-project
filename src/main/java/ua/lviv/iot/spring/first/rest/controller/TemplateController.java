package ua.lviv.iot.spring.first.rest.controller;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collection;

public interface TemplateController<T, ID> {
    ResponseEntity<T> getById(ID id);

    ResponseEntity<Collection<T>> getAll();

    ResponseEntity<T> create(T entity) throws IOException;

    ResponseEntity<T> update(ID id, T entity) throws IOException;

    ResponseEntity<T> delete(ID id) throws IOException;
}
