package ua.lviv.iot.spring.first.rest.writer;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface TemplateFileWriter<T, ID> {

    void save(T entity, String pathToFiles) throws IOException;

    void delete(ID id, File[] files) throws IOException;

    void update(ID id, T entity, File[] files) throws IOException;

    Map<ID, T> read(File monthDirectory) throws IOException;

}
