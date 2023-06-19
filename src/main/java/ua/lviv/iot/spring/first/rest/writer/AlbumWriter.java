package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Album;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class AlbumWriter implements TemplateFileWriter<Album, Integer> {

    @Override
    public void save(final Album album, final String pathToFiles) throws IOException {
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
                new FileOutputStream(pathToFiles, true), StandardCharsets.UTF_8))) {
            boolean fileExists = file.exists();

            if (!fileExists || file.length() == 0) {
                writer.println(album.getHeaders());
            }

            writer.println(album.toCSV());

            System.out.println("Album saved successfully.");
        }
    }


    @Override
    public void delete(final Integer id, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> lines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    lines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Album currentAlbum = extractFromCSV(line);
                        if (currentAlbum.getId().equals(id)) {
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
    }

    @Override
    public Map<Integer, Album> read(final File monthDirectory) throws IOException {
        Map<Integer, Album> albums = new HashMap<>();
        File[] files = monthDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

                    reader.readLine();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Album album = extractFromCSV(line);
                        albums.put(album.getId(), album);
                    }
                }
            }
        }
        return albums;
    }

    @Override
    public void update(final Integer id, final Album updatedAlbum, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> updatedLines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    updatedLines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Album currentAlbum = extractFromCSV(line);
                        if (currentAlbum.getId().equals(id)) {
                            updatedLines.add(updatedAlbum.toCSV());
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
    }

    private Album extractFromCSV(final String lineFromCSV) {
        String[] fields = lineFromCSV.split(",", 5);
        Integer id = Integer.valueOf(fields[0].trim());
        String title = fields[1].trim();
        String genre = fields[2].trim();
        Integer releaseYear = Integer.valueOf(fields[3].trim());
        String songsIdsString = fields[4].replaceAll("\\[|\\]", "");
        String[] songsIdArray = songsIdsString.split(",");
        List<Integer> songsId = new ArrayList<>();
        for (String songId : songsIdArray) {
            songsId.add(Integer.valueOf(songId.trim()));
        }

        return new Album(id, title, genre, releaseYear, songsId);
    }
}
