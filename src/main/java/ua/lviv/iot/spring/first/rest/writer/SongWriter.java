package ua.lviv.iot.spring.first.rest.writer;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Song;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Component
public class SongWriter extends CustomFileWriter<Song> {
    private static final String FILE_PREFIX = "song-";
    private String fileDirectory = "src/main/resources/songs";
    private List<File> files = findInputFiles(FILE_PREFIX, fileDirectory);

    public SongWriter() {
        super(FILE_PREFIX);
        this.files = files;
        createDirectoryIfNotExists(fileDirectory);
        this.setFileName(generateFileName());
    }

    public SongWriter(String fileName, List<File> files) {
        super("");
        this.setFileName(fileName);
        this.files = files;
    }

    @Override
    public void save(final Song song) throws IOException {
        String filePath = fileDirectory + "/" + getFileName();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            File file = new File(filePath);
            boolean fileExists = file.exists();

            if (!fileExists || file.length() == 0) {
                writer.println(song.getHeaders());
            }

            writer.println(song.toCSV());

            System.out.println("Song saved successfully.");
        }
    }

    @Override
    public void delete(final Integer id) throws IOException {

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

    @Override
    public Map<Integer, Song> read() throws IOException {
        Map<Integer, Song> songs = new HashMap<>();

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
        return songs;
    }

    @Override
    public void update(final Integer id, final Song updatedSong) throws IOException {
        boolean updated = false;

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
                        updated = true;
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

            if (updated) {
                System.out.println("Song updated successfully in file: " + file.getName());
                break;
            }
        }
    }

    private static Song extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", -1);
        Integer id = Integer.parseInt(fields[0].trim());
        String title = fields[1].trim();
        String duration = fields[2].trim();
        int releaseYear = Integer.parseInt(fields[3].trim());
        String lyrics = fields[4].trim();
        return new Song(id, title, duration, releaseYear, lyrics);
    }
}
