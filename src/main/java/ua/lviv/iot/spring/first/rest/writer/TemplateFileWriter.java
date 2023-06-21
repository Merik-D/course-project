package ua.lviv.iot.spring.first.rest.writer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TemplateFileWriter<T, ID> {

    public void save(T entity, String pathToFiles) throws IOException{
        File file = new File(pathToFiles);
        File parentDir = file.getParentFile();

        if (!parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (!dirCreated) {
                System.err.println("Failed to create parent directories for file: " + pathToFiles);
                return;
            }
        }

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(file, true), StandardCharsets.UTF_8))) {
            boolean fileExists = file.exists();

            if (!fileExists || file.length() == 0) {
                writer.println(getHeaders(entity));
            }

            writer.println(convertToCSV(entity));
        }
    };

    public void delete(ID id, File[] files) throws IOException{
        if (files != null) {
            for (File file : files) {
                List<String> lines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    lines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        T currentEntity = extractFromCSV(line);
                        if (getIdFromEntity(currentEntity).equals(id)) {
                            continue;
                        }
                        lines.add(line);
                    }
                }

                try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8))) {
                    for (String updatedLine : lines) {
                        writer.println(updatedLine);
                    }
                }
            }
        }
    };

    public void update(ID id, T entity, File[] files) throws IOException{
        if (files != null) {
            for (File file : files) {
                List<String> updatedLines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    updatedLines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        T currentEntity = extractFromCSV(line);
                        if (getIdFromEntity(currentEntity).equals(id)) {
                            updatedLines.add(convertToCSV(entity));
                        } else {
                            updatedLines.add(line);
                        }
                    }
                }

                try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                        new FileOutputStream(file), StandardCharsets.UTF_8))) {
                    for (String updatedLine : updatedLines) {
                        writer.println(updatedLine);
                    }
                }
            }
        }
    };

    public Map<ID, T> read(File monthDirectory) throws IOException{
        Map<ID, T> entities = new HashMap<>();
        File[] files = monthDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

                    reader.readLine();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        T entity = extractFromCSV(line);
                        entities.put(getIdFromEntity(entity), entity);
                    }
                }
            }
        }
        return entities;
    };

    protected abstract String getHeaders(T entity);

    protected abstract T extractFromCSV(String lineFromCSV);

    protected abstract String convertToCSV(T entity);

    protected abstract ID getIdFromEntity(T entity);

}
