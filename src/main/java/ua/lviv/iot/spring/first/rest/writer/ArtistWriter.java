package ua.lviv.iot.spring.first.rest.writer;

import lombok.Setter;
import org.springframework.stereotype.Component;
import ua.lviv.iot.spring.first.rest.models.Artist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Component
public class ArtistWriter implements TemplateFileWriter<Artist, Integer> {

    @Override
    public void save(final Artist artist, final String pathToFiles) throws IOException {
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
                writer.println(artist.getHeaders());
            }

            writer.println(artist.toCSV());

            System.out.println("Artist saved successfully.");
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
                        Artist currentArtist = extractFromCSV(line);
                        if (currentArtist.getId().equals(id)) {
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
    public Map<Integer, Artist> read(final File monthDirectory) throws IOException {
        Map<Integer, Artist> artists = new HashMap<>();
        File[] files = monthDirectory.listFiles();

        if (files != null) {
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {

                    reader.readLine();

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Artist artist = extractFromCSV(line);
                        artists.put(artist.getId(), artist);
                    }
                }
            }
        }
        return artists;
    }

    @Override
    public void update(final Integer id, final Artist updatedArtist, final File[] files) throws IOException {
        if (files != null) {
            for (File file : files) {
                List<String> updatedLines = new ArrayList<>();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8))) {
                    String headerLine = reader.readLine();
                    updatedLines.add(headerLine);

                    String line;
                    while ((line = reader.readLine()) != null) {
                        Artist currentArtist = extractFromCSV(line);
                        if (currentArtist.getId().equals(id)) {
                            updatedLines.add(updatedArtist.toCSV());
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

    private static Artist extractFromCSV(final String lineFromCSV) throws NumberFormatException {
        String[] fields = lineFromCSV.split(",", 4);
        Integer id = Integer.parseInt(fields[0].trim());
        String name = fields[1].trim();
        LocalDate dateOfBirth = LocalDate.parse(fields[2].trim());

        String albumsIdsString = fields[3].replaceAll("\\[|\\]", "");
        String[] albumsIdArray = albumsIdsString.split(",");
        List<Integer> albumsId = new ArrayList<>();
        for (String artistId : albumsIdArray) {
            if (!artistId.trim().isEmpty()) {
                albumsId.add(Integer.parseInt(artistId.trim()));
            }
        }
        return new Artist(id, name, dateOfBirth, albumsId);
    }


}
