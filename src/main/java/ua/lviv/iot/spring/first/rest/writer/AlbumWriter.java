package ua.lviv.iot.spring.first.rest.writer;

import lombok.Setter;
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

@Setter
@Component
public class AlbumWriter extends CustomFileWriter<Album> {
    private static final String FILE_PREFIX = "album-";
    private String fileDirectory = "src/main/resources/album";
    private List<File> files = findInputFiles(FILE_PREFIX, fileDirectory);

    public AlbumWriter() {
        super(FILE_PREFIX);
        this.files = files;
        createDirectoryIfNotExists(fileDirectory);
        this.setFileName(generateFileName());
    }

    public AlbumWriter(String fileName, List<File> files) {
        super("");
        this.setFileName(fileName);
        this.files = files;
    }

    @Override
    public void save(final Album album) throws IOException {
        String filePath = fileDirectory + "/" + getFileName();
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            File file = new File(filePath);
            boolean fileExists = file.exists();

            if (!fileExists || file.length() == 0) {
                writer.println(album.getHeaders());
            }

            writer.println(album.toCSV());
            System.out.println("Album saved successfully.");
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

    @Override
    public Map<Integer, Album> read() throws IOException {
        Map<Integer, Album> albums = new HashMap<>();

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

        return albums;
    }

    @Override
    public void update(final Integer id, final Album updatedAlbum) throws IOException {
        boolean updated = false;

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
                System.out.println("Album updated successfully in file: " + file.getName());
                break;
            }
        }
    }

    private static Album extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", -1);
        Integer id = Integer.parseInt(fields[0].trim());
        String title = fields[1].trim();
        String genre = fields[2].trim();
        int releaseYear = 0;
        releaseYear = Integer.parseInt(fields[3].trim());
        String songsIdsString = fields[4].replaceAll("\\[|\\]", "");
        String[] songsIdArray = songsIdsString.split(",");
        List<Integer> songsId = new ArrayList<>();
        for (String songId : songsIdArray) {
            songsId.add(Integer.parseInt(songId.trim()));
        }

        return new Album(id, title, genre, releaseYear, songsId);
    }
}
