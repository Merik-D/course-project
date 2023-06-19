package ua.lviv.iot.spring.first.rest.writer;

import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Song;

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

@Component
public class SongWriter implements TemplateFileWriter<Song, Integer> {

    @Override
    public void save(final Song song, final String pathToFiles) throws IOException {
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
                writer.println(song.getHeaders());
            }

            writer.println(song.toCSV());

            System.out.println("Song saved successfully.");
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
                        Song currentSong = extractFromCSV(line);
                        if (currentSong.getId().equals(id)) {
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
    public void update(final Integer id, final Song updatedSong, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> updatedLines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    updatedLines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Song currentSong = extractFromCSV(line);
                        if (currentSong.getId().equals(id)) {
                            updatedLines.add(updatedSong.toCSV());
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

    @Override
    public Map<Integer, Song> read(final File monthDirectory) throws IOException {
        Map<Integer, Song> songs = new HashMap<>();
        File[] files = monthDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

                    reader.readLine();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Song song = extractFromCSV(line);
                        songs.put(song.getId(), song);
                    }
                }
            }
        }
        return songs;
    }

    private Song extractFromCSV(final String lineFromCSV) {
        String[] fields = lineFromCSV.split(",", 5);
        Integer id = Integer.valueOf(fields[0].trim());
        String title = fields[1].trim();
        String duration = fields[2].trim();
        int releaseYear = Integer.parseInt(fields[3].trim());
        String lyrics = fields[4].trim();
        return new Song(id, title, duration, releaseYear, lyrics);
    }
}
